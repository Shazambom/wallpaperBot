import glob       #to get filenames
import datetime   #to check in weekly folder paths
import pyimgur    #to consolidate images to albums in imgur
import praw       #to post links to reddit/r/slashw
import OAuth2Util #OAuth2 for reddit
import os         #to check for empty files
import requests   #to see how many imgur requests remain


def assign_directory_by_time():
    """
    Returns string depicting the current week of the year. For example,
    "Y2016-W20" for the 20th week in 2016
    """
    path = 'Y'
    date = datetime.datetime.now()
    path = path + str(date.year)
    path = path + '-W' + str(date.isocalendar()[1])  #the week of the year
    return path


PATH_TO_CONFIG = '/home/pi/GitHub/wallpaperBot/config.txt'

#required by reddit when using a bot
USER_AGENT = '4chan /w/ crossposter for /u/Shazambom'

#subreddit name - www.reddit.com/r/slashw
SUBREDDIT = 'slashw'

#path for the folder for this week in the raspberry pi
PATH_BASE = '/media/UNTITLED/Wallpapers/' + assign_directory_by_time()

config = open(PATH_TO_CONFIG, 'r')
#for accessing the imgur api
CLIENT_ID = config.readline()
CLIENT_SECRET = config.readline()
config.close()
imgur = pyimgur.Imgur(CLIENT_ID, CLIENT_SECRET)


def consolidate_to_albums():
    filenames = glob.glob(PATH_BASE + '/*.txt')

    r = praw.Reddit(user_agent=USER_AGENT)
    o = OAuth2Util.OAuth2Util(r)
    o.refresh(force=True)
    print('I am logged in to reddit')

    for filename in filenames:
        images = []
        if is_empty(filename):
           os.remove(filename)
           continue

        urls = open(filename, 'r')

        for url in urls:
            images.append(imgur.get_at_url(url))

        title = filename.rsplit('/', 1)[1][:-4]
        album = imgur.create_album(title=title, images=images)
        print('album made at ' + album.link)

        r.submit(SUBREDDIT, album.title, url=album.link)
        print ('post submitted')

        urls.close()
        os.remove(filename)

def is_empty(filename):
    return os.stat(filename).st_size==0

print(assign_directory_by_time())
consolidate_to_albums()
