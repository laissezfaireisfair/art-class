from ultralytics import YOLO

model = YOLO("yolov8n-cls.yaml")

model.train(data="dataset/", epochs=1, imgsz=64)
# model.train(data="mnist160", epochs=1000, imgsz=64)
