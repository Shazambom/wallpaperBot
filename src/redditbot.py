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
    filenames = glob.glob(PATH_BASE + '*.txt')

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
        album = imgur.upload_album(title=title, images=images)
        print('album made at ' + album.link)

        r.submit(SUBREDDIT, album.title, url=album.link)
        print ('post submitted')

        urls.close()
        os.remove(filename)

def is_empty(filename):
    return os.stat(filename).st_size==0
