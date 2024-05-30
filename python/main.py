import torch
import torch.nn as nn
import torch.nn.functional as F
from ultralytics.nn.tasks import ClassificationModel
from ultralytics.models.yolo.classify import ClassificationTrainer


class ArcFace(nn.Module):
    def __init__(self, feature_in, feature_out=24, margin=0.3, scale=64):
        super().__init__()
        self.feature_in = feature_in
        self.feature_out = feature_out
        self.scale = scale
        self.margin = margin

        self.weights = nn.Parameter(torch.FloatTensor(feature_out, feature_in))
        nn.init.xavier_normal_(self.weights)

    def forward(self, features, targets):
        cos_theta = F.linear(features, F.normalize(self.weights), bias=None)
        cos_theta = cos_theta.clip(-1 + 1e-7, 1 - 1e-7)
        arc_cos = torch.acos(cos_theta)
        arc_cos += F.one_hot(targets, num_classes=self.feature_out) * self.margin
        cos_theta_2 = torch.cos(arc_cos)
        return cos_theta_2 * self.scale


class Loss:
    def __call__(self, preds, batch):
        #     arch_face = ArcFace(feature_in=batch.get_shape(), feature_out=100)
        #     return arch_face.forward(batch, preds)
        loss = F.cross_entropy(preds, batch["cls"], reduction="mean")
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
    'epochs': 3,
    'imgsz': 640,
    'name': 'trainer_n_3_epoch',
    'model': 'yolov8n-cls.yaml'
}
trainer = CustomTrainer(overrides=params)
trainer.train()
