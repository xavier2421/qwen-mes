from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from datetime import datetime
from ..core.database import get_db
from ..models import ProductionTask, WorkRecord, User
from ..schemas import (
    WorkRecordCreate, WorkRecordStart, WorkRecordEnd, WorkRecordResponse,
    ResponseBase
)

router = APIRouter(prefix="/production", tags=["生产管理"])


@router.post("/work-record/start", response_model=ResponseBase)
def start_work(
    work_data: WorkRecordStart,
    db: Session = Depends(get_db)
):
    """开始工序报工"""
    # 验证任务是否存在
    task = db.query(ProductionTask).filter(ProductionTask.id == work_data.task_id).first()
    if not task:
        raise HTTPException(status_code=404, message="生产任务不存在")
    
    # 验证工人是否存在
    worker = db.query(User).filter(User.id == work_data.worker_id).first()
    if not worker:
        raise HTTPException(status_code=404, message="工人不存在")
    
    # 创建工时记录
    db_record = WorkRecord(
        tenant_id=task.tenant_id,
        task_id=work_data.task_id,
        worker_id=work_data.worker_id,
        process_name=work_data.process_name,
        process_index=work_data.process_index,
        start_time=datetime.utcnow(),
        status="in_progress"
    )
    db.add(db_record)
    
    # 更新任务状态和当前工序
    task.status = "processing"
    task.current_process_index = work_data.process_index
    if not task.actual_start:
        task.actual_start = datetime.utcnow()
    
    db.commit()
    db.refresh(db_record)
    
    return ResponseBase(
        message="开始报工成功",
        data={"record_id": db_record.id}
    )


@router.post("/work-record/end/{record_id}", response_model=ResponseBase)
def end_work(
    record_id: int,
    work_data: WorkRecordEnd,
    db: Session = Depends(get_db)
):
    """结束工序报工"""
    record = db.query(WorkRecord).filter(WorkRecord.id == record_id).first()
    if not record:
        raise HTTPException(status_code=404, message="工时记录不存在")
    
    # 更新记录
    record.end_time = datetime.utcnow()
    record.quantity = work_data.quantity
    record.qualified_quantity = work_data.qualified_quantity or work_data.quantity
    record.defect_quantity = work_data.defect_quantity or 0
    record.defect_reason = work_data.defect_reason
    record.status = "completed"
    
    # 计算实际工时
    if record.start_time:
        delta = record.end_time - record.start_time
        record.actual_hours = round(delta.total_seconds() / 3600, 2)
    
    # 更新任务进度
    task = db.query(ProductionTask).filter(ProductionTask.id == record.task_id).first()
    if task:
        # 检查是否所有工序完成
        if task.process_route_id:
            process_route = db.query(ProcessRoute).filter(
                ProcessRoute.id == task.process_route_id
            ).first()
            if process_route and record.process_index >= len(process_route.processes) - 1:
                task.status = "completed"
                task.actual_end = datetime.utcnow()
    
    db.commit()
    
    return ResponseBase(message="结束报工成功")


@router.get("/tasks", response_model=ResponseBase)
def list_tasks(
    skip: int = 0,
    limit: int = 100,
    status_filter: str = None,
    tenant_id: int = 1,
    db: Session = Depends(get_db)
):
    """获取生产任务列表"""
    query = db.query(ProductionTask).filter(
        ProductionTask.tenant_id == tenant_id
    )
    
    if status_filter:
        query = query.filter(ProductionTask.status == status_filter)
    
    tasks = query.order_by(ProductionTask.created_at.desc()).offset(skip).limit(limit).all()
    total = query.count()
    
    return ResponseBase(
        message="获取成功",
        data={"items": tasks, "total": total}
    )


@router.get("/tasks/{task_id}/progress", response_model=ResponseBase)
def get_task_progress(task_id: int, db: Session = Depends(get_db)):
    """获取任务进度详情"""
    task = db.query(ProductionTask).filter(ProductionTask.id == task_id).first()
    if not task:
        raise HTTPException(status_code=404, message="任务不存在")
    
    # 获取该任务的所有工时记录
    work_records = db.query(WorkRecord).filter(
        WorkRecord.task_id == task_id
    ).order_by(WorkRecord.process_index).all()
    
    return ResponseBase(
        message="获取成功",
        data={
            "task": task,
            "work_records": work_records
        }
    )


# 需要导入 ProcessRoute
from ..models import ProcessRoute
