from pytorch_metric_learning import losses
from ultralytics.nn.tasks import ClassificationModel
from ultralytics.models.yolo.classify import ClassificationTrainer


class Loss:
    def __call__(self, preds, batch):
        loss_func = losses.ArcFaceLoss(num_classes=100, embedding_size=preds.shape[1])
        loss = loss_func(preds, batch['cls'])
        loss_items = loss.detach()
        return loss, loss_items


class ArcFaceModel(ClassificationModel):
    def init_criterion(self):
        return Loss()


class CustomTrainer(ClassificationTrainer):
    def get_model(self, cfg=None, weights=None, verbose=True):
        return ArcFaceModel(cfg=cfg, verbose=verbose)


params = {
    'data': '../dataset_top_100_authors/',
    'epochs': 50,
    'imgsz': 640,
    'name': 'arc_face_n_50',
    'model': 'yolov8n-cls.yaml'
}
trainer = CustomTrainer(overrides=params)
trainer.train()
