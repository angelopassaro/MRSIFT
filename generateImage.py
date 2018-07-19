from __future__ import print_function

import concurrent.futures
import random
import time
from os import listdir

import numpy as np
from PIL import Image

BASE_PATH = "/home/angelo/Documents/Universita/SOA/Progetto/"


def resize(image_object):
    resize = [0, 30, 60, 90, 180]
    seed = random.choice(resize)
    new_size = seed // 100 + image_object.size[0] if seed != 0 else image_object.size[0]
    return image_object.resize((new_size, new_size))


def scale(image_object):
    scale = [15, 30, 50]
    return image_object.rotate(random.choice(scale), expand=True)


def mask(new_image_object):
    threshold = 100
    dist = 5
    arr = np.array(np.asarray(new_image_object))
    r, g, b, a = np.rollaxis(arr, axis=-1)
    mask = ((r > threshold)
            & (g > threshold)
            & (b > threshold)
            & (np.abs(r - g) < dist)
            & (np.abs(r - b) < dist)
            & (np.abs(g - b) < dist)
            )
    arr[mask, 3] = 0
    return Image.fromarray(arr, mode='RGBA')


def generate(number, files):
    image_copy = Image.open(BASE_PATH + 'Sfondo/Sky.jpg').copy()

    for _ in range(4):

        image_object = Image.open(
            BASE_PATH + 'Objects/%s' % random.choice(files)).convert("RGBA")

        if _ == 0 or _ == 2:
            new_image_object = resize(image_object)
        else:
            new_image_object = scale(image_object)

        img = mask(new_image_object)
        position = (random.randrange(image_copy.width) - new_image_object.width // 2,
                    (random.randrange(image_copy.height)) - new_image_object.height // 2)
        image_copy.paste(img, position, img)

    image_copy.save(BASE_PATH + 'Scenes2/test%s.jpg' % number)


if __name__ == '__main__':
    start = time.time()

    files = [f for f in listdir(BASE_PATH + "Objects/")]

    with concurrent.futures.ThreadPoolExecutor(max_workers=6) as executor:
        futures = {executor.submit(generate, _, files) for _ in range(5)}
        concurrent.futures.wait(futures)

    print("Times %ssecs" % (time.time() - start))
