import os
import glob
import praw       
import OAuth2Util 
import requests
import shutil
import copy
from base64 import b64encode
from time import sleep, time

TEST = False
PATH_TO_CONFIG = ""
PATH_BASE = ""

if TEST:
    PATH_TO_CONFIG = '/Users/ian/Projects/wallpaperBot/config.txt'
    PATH_BASE = '/Users/ian/Desktop/RippedWallpapers/'
else:
    PATH_TO_CONFIG = '/home/pi/GitHub/wallpaperBot/config.txt'
    PATH_BASE = '/media/UNTITLED/Wallpapers/'

DEFAULT_ALBUM_TITLE = "/R/SLASHW"
UPLOAD_LIMIT = 1200  
REQUEST_LIMIT = 12250
REQUEST_COUNTER = 0
SECONDS_LIMIT = 82800
MIN_THREAD_SIZE = 25

USER_AGENT = '4chan /w/ crossposter for /u/Shazambom'
SUBREDDIT = 'slashw'

config = open(PATH_TO_CONFIG, 'r')
CLIENT_ID = config.readline()[:-1]  # remove the \n at the end
CLIENT_SECRET = config.readline()[:-1]  # remove the \n at the end
ACCESS_TOKEN = None
REFRESH_TOKEN = None
config.close()
imgUrl = "https://api.imgur.com/3/image"
albumUrl = "https://api.imgur.com/3/album"
imgurHeader = {'Authorization': "Client-ID "+str(CLIENT_ID)}
imgurAlbumUrl = "https://imgur.com/a/"


def upload_images(album_files, REQUEST_COUNTER):
    images = []
    toDelete = []
    for filename in album_files:
        for i in range(0, 3):
            try:
                img = open(filename, 'rb')
                data = {'key': CLIENT_SECRET, 'image': b64encode(img.read()), 'type': 'base64'}
                REQUEST_COUNTER += 1
                resp = requests.post(imgUrl, headers=imgurHeader, data=data)
                img.close()
                content = resp.json()
                if not content['success']:
                    print(content['data']['error'])
                if content['status'] == 400:
                    try:
                        print("Sleeping for a bit, nighty night")
                        timeToSleep = int(content['data']['error'][40:42].strip())
                        sleep((timeToSleep + 1)* 60)
                    except:
                        pass
                if content['success']:
                    sleep(0.25)
                    images.append(content['data']['id'])
                    toDelete.append(filename)
                    print "Uploaded image: " + content['data']['link']
                    break
            except Exception as err:
                print(err)
        if REQUEST_COUNTER >= REQUEST_LIMIT:
            break
    print("Uploaded: " + str(len(images)) + " images")
    return (images, toDelete)

def upload_album(title=DEFAULT_ALBUM_TITLE, album_files=[]):
    global REQUEST_COUNTER
    images = []
    toDelete = []
    if REQUEST_COUNTER < REQUEST_LIMIT:
        images, toDelete = upload_images(album_files, REQUEST_COUNTER)
    if len(images) > 0:
        for i in range(0, 3):
            try:
                REQUEST_COUNTER += 1
                resp = requests.post(albumUrl, headers=imgurHeader, data={'key': CLIENT_SECRET, 'ids[]': images, 'title': title})
                content = resp.json()
                if content['success']:
                    for filename in toDelete:
                        try:
                            os.remove(filename)
                        except:
                            pass
                    return((imgurAlbumUrl+str(content['data']['id']), len(images)))
            except Exception as err:
                print(err)
    return((None, 0))


def get_image_filenames(path_base):
    JPG_PATH = path_base + '*.jpg'
    JPEG_PATH = path_base + '*.jpeg'
    PNG_PATH = path_base + '*.png'
    APNG_PATH = path_base + '*.apng'
    BMP_PATH = path_base + '*.bmp'
    TIFF_PATH = path_base + '*.tiff'
    TIF_PATH = path_base + '*.tif'
    XCF_PATH = path_base + '*.xcf'
    PDF_PATH = path_base + '*.pdf'

    filenames = []
    filenames.extend(glob.glob(JPG_PATH))
    filenames.extend(glob.glob(JPEG_PATH))
    filenames.extend(glob.glob(PNG_PATH))
    filenames.extend(glob.glob(APNG_PATH))
    filenames.extend(glob.glob(BMP_PATH))
    filenames.extend(glob.glob(TIF_PATH))
    filenames.extend(glob.glob(TIFF_PATH))
    filenames.extend(glob.glob(PDF_PATH))
    filenames.extend(glob.glob(XCF_PATH))
    if len(filenames) == 0:
        shutil.rmtree(path_base[:-1])
    return filenames

def get_thread_name(filename):
    """
    takes string filename and returns string thread name
    thread name is everything before the last '_' and after the last '/'
    """
    return filename.split('_', 1)[0].rsplit('/', 1)[-1]
def get_folders():
    return [PATH_BASE+x for x in os.listdir(PATH_BASE) if os.path.isdir(PATH_BASE+x)]

def get_valid_filenames():
    folders = get_folders()
    print("Gathered folders")
    filenames = []
    for folder in folders:
        filenames.extend(get_image_filenames(folder+"/"))
        if len(filenames) > UPLOAD_LIMIT:
            filenames = filenames[:UPLOAD_LIMIT]
            break
    print("Gathered file names")
    return filenames
def create_threads(filenames):
    dic = {}
    for filename in filenames:
        threadname = get_thread_name(filename)
        if threadname not in dic:
            dic[threadname] = []
        dic[threadname].append(filename)
    return dic

def reddit_login(reddit):
    o = OAuth2Util.OAuth2Util(reddit)
    o.refresh(force=True)
    print('I am logged in to reddit')

def __main__():
    begining = time()
    reddit = praw.Reddit(user_agent=USER_AGENT)
    reddit_login(reddit)
    filenames = get_valid_filenames()
    print "Number of files to be uploaded:", str(len(filenames))
    threads = create_threads(filenames)

    for thread in copy.copy(threads):
        if(len(threads[thread]) < MIN_THREAD_SIZE):
            del threads[thread]
    while True:
        notTooLong = True
        keys = list(threads.keys())
        for thread in keys:
            if len(threads[thread]) > 150:
                    notTooLong = False
                    threads[thread+'-cont'] = threads[thread][150:]
                    threads[thread] = threads[thread][:150]
        if notTooLong:
            break
    print "Number of threads created:", str(len(threads))
    numImages = 0
    for thread in threads:
        link, imgUploaded = upload_album(thread, threads[thread])
        if link is not None:
            for i in range(0,3):
                try:
                    reddit.submit(SUBREDDIT, thread.replace("-", " "), url=link)
                    numImages += imgUploaded
                    break
                except Exception as err:
                    print(err)
                    sleep(1)
            print "Submitted:", thread, ":", link
        if (time() - begining) > SECONDS_LIMIT:
            break
    print("Time to execute: "+ str(time() - begining))
    print("Number of Images uploaded and submitted: " + str(numImages))

    
if __name__ == "__main__":
    __main__()


