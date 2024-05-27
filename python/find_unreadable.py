import cv2
import os


def walk_through_files(path):
    for (dir_path, _, filenames) in os.walk(path):
        for filename in filenames:
            yield os.path.join(dir_path, filename)


for image_path in walk_through_files('dataset/'):
    try:
        image = cv2.imread(str(image_path))
        frame_HSV = cv2.cvtColor(image, cv2.COLOR_RGB2HSV)
    except:
        print(image_path)

cv2.destroyAllWindows()
