import pyimgur
import gdata.gauth

# PATH_TO_CONFIG = '/home/pi/GitHub/wallpaperBot/config.txt'
PATH_TO_CONFIG = '/home/yash/PycharmProjects/wallpaperBot/config.txt'
DEFAULT_ALBUM_TITLE = "/R/SLASHW"
UPLOAD_LIMIT = 1000  # don't upload more that a thousand images in a day

config = open(PATH_TO_CONFIG, 'r')
CLIENT_ID = config.readline()[:-1]  # remove the \n at the end
CLIENT_SECRET = config.readline()[:-1]  # remove the \n at the end
config.close()

class Imgur:

    def __init__(self):
        self.imgur = pyimgur.Imgur(CLIENT_ID, CLIENT_SECRET)

    def upload_image(self, filename):
        return self.imgur.upload_image(filename).link

    def upload_album(self, title=DEFAULT_ALBUM_TITLE, image_list=[]):
        # FIXME: get_at_url will only work with 'http://imgur.com/5qIObqw' type of link
        # FIXME: will not work with 'http://i.imgur.com/5qIObqw.jpg' that we are supplying
        images = [self.imgur.get_image(Imgur.get_id(url)) for url in image_list]
        return self.imgur.create_album(title, images).link

    @staticmethod
    def get_id(url):
        return url[:-4].rsplit('/', 1)[1]


class Picasa:

    def __init__(self):
        SCOPES = ['http://picasaweb.google.com/data/']
        USER_AGENT = '4chan to reddit crossposter bot - /r/slashw'
        token = gdata.gauth.OAuth2Token(client_id=CLIENT_ID,
                                        client_secret=CLIENT_SECRET,
                                        scope=' '.join(SCOPES),
                                        user_agent=USER_AGENT)



    #How to create a client (from google example):
    # gd_client = gdata.photos.service.PhotosService()

    #How to create a new album (from google example):
    # album = gd_client.InsertAlbum(title='New album', summary='This is an album')

    #How to upload a photo (from google example):
    # photo = gd_client.InsertPhotoSimple(album_url, 'New Photo',
    #     'Uploaded using the API', filename, content_type='image/jpeg')
