import glob       #to get filenames
import datetime       #to generate weekly folder paths
import pyimgur    #to upload files to imgur
import praw       #to post links to reddit/r/slashw

# /media/UNTITLED/Wallpapers/

def assign_directory_by_time():
    path = 'Y'
    date = datetime.datetime.now()
    path = path + str(date.year)
    path = path + '_W' + str(date.isocalendar()[1])  #the week of the year
    return path

CLIENT_ID = '2e6582b4e4109df'
CLIENT_SECRET = '9a0e29fb2220d772a81a56a0d3a4f9fee9d8b29b'

PATH_BASE = '/home/yash/Ian4chanProject/Images/' + assign_directory_by_time()
JPG_PATH = PATH_BASE + '/*.jpg'
JPEG_PATH = PATH_BASE + '/*.jpeg'
PNG_PATH = PATH_BASE + '/*.png'
APNG_PATH = PATH_BASE + '/*.apng'
BMP_PATH = PATH_BASE + '/*.bmp'
TIFF_PATH = PATH_BASE + '/*.tiff'
TIF_PATH = PATH_BASE + '/*.tif'
XCF_PATH = PATH_BASE + '/*.xcf'
PDF_PATH = PATH_BASE + '/*.pdf'

USER_AGENT = '4chan /w/ crossposter for /u/Shazambom'
USERNAME = 'SmallTextReader'
PASSWORD = '9AyEXPga2JS8'
SUBREDDIT = 'slashw'


imgur = pyimgur.Imgur(CLIENT_ID, CLIENT_SECRET)
image_links = []



def upload_to_imgur():
    
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
    
    threads = []
    already_done = []
    for filename in filenames:
        thread = filename.split('_', 1)[0]  #returns everything before the first encountered underscore
        if (thread not in already_done):
            album = []
            album2 = []
            for filename2 in filenames:
                thread2 = filename2.split('_', 1)[0]
                if (thread == thread2):
                    if (album.length > 149):
                        album2.append(filename2)
                    else:
                        album.append(filename2)
            album_images = []
            album2_images = []
            for image_filename in album:
                album_images.append(imgur.upload_image(image_filename))
                print ('uploaded')
            if (album.length > 149):
                for image_filename in album2:
                    album2_images.append(imgur.upload_image(image_filename))
                    print ('uploaded')
            album_title = thread.rsplit('/', 1)[-1]  #removes everything before last '/'
            image_links.append(imgur.create_album(title=album_title, images=album_images).link)
            print ('album created')
            if (album.length > 149):
                image_links.append(imgur.create_album(title=album_title, images=album2_images).link)
                print ('album2 created')
            already_done.append(thread)


def post_to_reddit():
    r = praw.Reddit(user_agent=USER_AGENT)
    r.login(USERNAME, PASSWORD, disable_warning=True)
    for image_link in image_links:
        album = imgur.get_at_url(image_link)
        r.submit(SUBREDDIT, album.title, url=album.link)
        print ('post submitted')


upload_to_imgur()
post_to_reddit()
