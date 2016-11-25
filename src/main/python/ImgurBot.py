import os
import glob
import praw       
import OAuth2Util 
import requests
from base64 import b64encode
from time import sleep

# PATH_TO_CONFIG = '/home/pi/GitHub/wallpaperBot/config.txt'
PATH_TO_CONFIG = '/Users/ian/Projects/wallpaperBot/config.txt'
# PATH_TO_CONFIG = '/home/yash/PycharmProjects/wallpaperBot/config.txt'
DEFAULT_ALBUM_TITLE = "/R/SLASHW"
UPLOAD_LIMIT = 1250  
REQUEST_LIMIT = 12500
# PATH_BASE = '/media/UNTITLED/Wallpapers/'
PATH_BASE = '/Users/ian/Desktop/RippedWallpapers/'
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



def upload_images(album_files):
    images = []
    toDelete = []
    for filename in album_files:
        for i in range(0, 3):
            img = open(filename, 'rb')
            data = {'key': CLIENT_SECRET, 'image': b64encode(img.read()), 'type': 'base64'}
            resp = requests.post(imgUrl, headers=imgurHeader, data=data)
            img.close()
            content = resp.json()
            if content['status'] is not 200:
                print(content['data']['error'])
            if content['status'] == 400:
                print("Sleeping for a bit, nighty night")
                sleep(900)
            print(content)
            if content['success']:
                sleep(0.25)
                images.append(content['data']['id'])
                toDelete.append(filename)
                print "Uploaded image: " + content['data']['link']
                break
    print("Uploaded: " + str(len(images)) + " images")
    return (images, toDelete)

def upload_album(title=DEFAULT_ALBUM_TITLE, album_files=[]):
    images, toDelete = upload_images(album_files)
    if len(images) > 0:
        resp = requests.post(albumUrl, headers=imgurHeader, data={'key': CLIENT_SECRET, 'ids[]': images, 'title': title})
        content = resp.json()
        if content['success']:
            for filename in toDelete:
                os.remove(filename)
        return(imgurAlbumUrl+str(content['data']['id']))
    return(None)


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
        os.rmdir(path_base[:-1])
    return filenames

def get_thread_name(filename):
    """
    takes string filename and returns string thread name
    thread name is everything before the last '_' and after the last '/'
    """
    return filename.split('_', 1)[0].rsplit('/', 1)[-1]
def get_folders():
    return [x[0] for x in os.walk(PATH_BASE)][1:]

def get_valid_filenames():
    folders = get_folders()
    filenames = []
    for folder in folders:
        filenames.extend(get_image_filenames(folder+"/"))
        if len(filenames) > UPLOAD_LIMIT:
            filenames = filenames[:UPLOAD_LIMIT]
            break
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
    reddit = praw.Reddit(user_agent=USER_AGENT)
    reddit_login(reddit)
    filenames = get_valid_filenames()
    print "Number of files to be uploaded:", str(len(filenames))
    threads = create_threads(filenames)
    print "Number of threads created:", str(len(threads))
    for thread in threads:
        link = upload_album(thread, threads[thread])
        if link is not None:
            reddit.submit(SUBREDDIT, thread, url=link)
            print "Submitted:", thread, ":", link

    
if __name__ == "__main__":
    __main__()


