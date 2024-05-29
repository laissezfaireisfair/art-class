from ultralytics import YOLO

model = YOLO("yolov8n-cls.yaml")

model.train(data="../dataset/", epochs=300, imgsz=640, name="yolo_n_300_epochs")
