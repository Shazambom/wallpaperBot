import glob       #to get filenames
import datetime   #to check in weekly folder paths
import pyimgur    #to consolidate images to albums in imgur
import praw       #to post links to reddit/r/slashw
import os         #to check for empty files

def assign_directory_by_time():
    path = 'Y'
    date = datetime.datetime.now()
    path = path + str(date.year)
    path = path + '-W' + str(date.isocalendar()[1])  #the week of the year
    return path

CLIENT_ID = '2e6582b4e4109df'
CLIENT_SECRET = '9a0e29fb2220d772a81a56a0d3a4f9fee9d8b29b'

imgur = pyimgur.Imgur(CLIENT_ID, CLIENT_SECRET)


USER_AGENT = '4chan /w/ crossposter for /u/Shazambom'
USERNAME = 'SmallTextReader'
PASSWORD = '9AyEXPga2JS8'
SUBREDDIT = 'slashw'

album_links = []
PATH_BASE = '/media/UNTITLED/Wallpapers/' + assign_directory_by_time()

def consolidate_to_albums():
    filenames = glob.glob(PATH_BASE + '/*.txt')
    for filename in filenames:
        images = []
        
        if is_empty(filename):
           os.remove(filename)
           continue
        
        file = open(filename, 'r')
        
        for line in file:
            images.append(imgur.get_at_url(line))

        title = filename.rsplit('/', 1)[1][:-4]
        album_links.append(imgur.create_album(title=title, images=images))
        print('album made at ' + album_links[-1].link)
        
        file.close()
            
        
def is_empty(filename):
    return os.stat(filename).st_size==0

def post_to_reddit():
    r = praw.Reddit(user_agent=USER_AGENT)
    r.login(USERNAME, PASSWORD, disable_warning=True)
    for album_link in album_links:
        album = imgur.get_at_url(album_link)
        r.submit(SUBREDDIT, album.title, url=album.link)
        print ('post submitted')

    
consolidate_to_albums()
post_to_reddit()
