import glob       #to get filenames
import praw       #to post links to reddit/r/slashw
import OAuth2Util #OAuth2 for reddit
import os         #to check for empty files
import uploadbot

#required by reddit when using a bot
USER_AGENT = '4chan /w/ crossposter for /u/Shazambom'

#subreddit name - www.reddit.com/r/slashw
SUBREDDIT = 'slashw'


def consolidate_to_albums():

    r = praw.Reddit(user_agent=USER_AGENT)
    reddit_login()

    filenames = glob.glob(PATH_BASE + '*.txt')

    for filename in filenames:

        if is_empty(filename):
            os.remove(filename)
            continue

        urls = open(filename, 'r')

        images = []
        for url in urls:
            images.append(url)

        title = filename[:-4]  # remove '.txt' at the end
        album = uploadbot.image_uploader.upload_album(title=title, images=images)
        print('album made at ' + album)

        r.submit(SUBREDDIT, album.title, url=album.link)
        print ('post submitted')

        urls.close()
        os.remove(filename)


def reddit_login():
    o = OAuth2Util.OAuth2Util(r)
    o.refresh(force=True)
    print('I am logged in to reddit')


def is_empty(filename):
    return os.stat(filename).st_size==0
