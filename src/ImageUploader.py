import pyimgur
import gdata.gauth

# PATH_TO_CONFIG = '/home/pi/GitHub/wallpaperBot/config.txt'
PATH_TO_CONFIG = '/home/yash/PycharmProjects/wallpaperBot/config.txt'
DEFAULT_ALBUM_TITLE = "/R/SLASHW"
UPLOAD_LIMIT = 1000 # don't upload more that a thousand images in a day

config = open(PATH_TO_CONFIG, 'r')
CLIENT_ID = config.readline()
CLIENT_SECRET = config.readline()
config.close()


class ImageUploader:
    """
    Interface for using different api's to upload images to the internet
    """

    def upload_image(self, filename):
        pass

    def upload_album(self, title=DEFAULT_ALBUM_TITLE, image_list=[]):
        pass

    def get_image_at(self, url):
        pass


class Imgur(ImageUploader):

    def __init__(self):
        self.imgur = pyimgur.Imgur(CLIENT_ID, CLIENT_SECRET)

    def upload_image(self, filename):
        return self.imgur.upload_image(filename).link

    def upload_album(self, title=DEFAULT_ALBUM_TITLE, image_list=[]):
        return self.imgur.create_album(title, image_list).link


class Picasa(ImageUploader):

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
