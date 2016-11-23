# from imgurpython import ImgurClient
import pyimgur
import os
import glob
import praw       
import OAuth2Util 

# PATH_TO_CONFIG = '/home/pi/GitHub/wallpaperBot/config.txt'
PATH_TO_CONFIG = '/Users/ian/Projects/wallpaperBot/config.txt'
# PATH_TO_CONFIG = '/home/yash/PycharmProjects/wallpaperBot/config.txt'
DEFAULT_ALBUM_TITLE = "/R/SLASHW"
UPLOAD_LIMIT = 1250  # don't upload more that a thousand images in a day
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
# imgur_client = ImgurClient(CLIENT_ID, CLIENT_SECRET, ACCESS_TOKEN, REFRESH_TOKEN)
imgur_client = pyimgur.Imgur(CLIENT_ID, CLIENT_SECRET)



# def authenticate():
# 	# Get client ID and secret from auth.ini

# 	client = ImgurClient(CLIENT_ID, CLIENT_SECRET, ACCESS_TOKEN, REFRESH_TOKEN)

# 	# Authorization flow, pin example (see docs for other auth types)
# 	authorization_url = client.get_auth_url('pin')

# 	print("Go to the following URL: {0}".format(authorization_url))

# 	# Read in the pin, handle Python 2 or 3 here.
# 	pin = raw_input("Enter pin code: ")
# 	print("-" + str(pin) + "-")

# 	# ... redirect user to `authorization_url`, obtain pin (or code or token) ...
# 	# credentials = client.authorize(pin, 'pin')
# 	credentials = authorize(client, pin, 'pin')
# 	client.set_user_auth(credentials['access_token'], credentials['refresh_token'])

# 	print("Authentication successful! Here are the details:")
# 	print("   Access token:  {0}".format(credentials['access_token']))
# 	print("   Refresh token: {0}".format(credentials['refresh_token']))

# 	return client


def upload_images(album_files):
    images = []
    for filename in album_files:
        # images.append(imgur_client.upload_from_path(filename)['id'])
        images.append(imgur_client.upload_image(filename).id)
    print("Uploaded: " + str(len(images)) + " images")
    return images

def upload_album(title=DEFAULT_ALBUM_TITLE, album_files=[]):
	images = upload_images(album_files)
	# return imgur_client.create_album({"title":title, "ids": images})
	return imgur_client.create_album(title, images)

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
	print folders
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
		album = upload_album(thread, threads[thread])
		reddit.submit(SUBREDDIT, album.title, url=album.link)
		print "Submitted:", album.title, ":", album.link
	
if __name__ == "__main__":
	__main__()


