import glob       #to get filenames
import pyimgur    #to upload files to imgur
import praw       #to post links to reddit/r/slashw

CLIENT_ID = '2e6582b4e4109df'
CLIENT_SECRET = '9a0e29fb2220d772a81a56a0d3a4f9fee9d8b29b'
IMAGE_PATH = 'F:\\RippedWallpapers\\Y2015_W31\\*.*'

USER_AGENT = '4chan /w/ crossposter for /u/Shazambom'
USERNAME = 'SmallTextReader'
PASSWORD = '9AyEXPga2JS8'
SUBREDDIT = 'slashw'


imgur = pyimgur.Imgur(CLIENT_ID, CLIENT_SECRET)
image_links = []

def upload_to_imgur():
    filenames = glob.glob(IMAGE_PATH)
    threads = []
    already_done = []
    for filename in filenames:
        thread = filename.split('_', 1)[0]  #returns everything before the first encountered underscore
        if (thread not in already_done):
            album = []
            for filename2 in filenames:
                thread2 = filename2.split('_', 1)[0]
                if (thread == thread2):
                    album.append(filename2)
            album_images = []
            for image_filename in album:
                album_images.append(imgur.upload_image(image_filename))
                print ('uploaded')
            album_title = thread.rsplit('\\', 1)[-1]
            print (album_title)
            image_links.append(imgur.create_album(title=album_title, images=album_images).link)
            print ('album created')
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
