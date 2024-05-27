from ultralytics import YOLO

model = YOLO("yolov8n-cls.yaml")

model.train(data="dataset/", epochs=100, imgsz=640, name="hundredEpochDefaultYolo")
# model.train(data="mnist160", epochs=10, imgsz=64, name="mnistCheck")
