from ultralytics import YOLO

model = YOLO("yolov8n-cls.yaml")

model.train(data="dataset/", epochs=50, imgsz=640)
