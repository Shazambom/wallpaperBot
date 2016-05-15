import pyimgur    #to upload files to imgur
import gdata.photos.service

PATH_TO_CONFIG = '/home/pi/GitHub/wallpaperBot/config.txt'
DEFAULT_TITLE = "/R/SLASHW"
UPLOAD_LIMIT = 1000 # don't upload more that a thousand images in a day


class ImageUploader:

    def upload_image(filename):
        pass

    def upload_album(title=DEFAULT_TITLE, image_list=[]):
        pass

    def get_image_at(url):
        pass


class Imgur(ImageUploader):

    def __init__():
        config = open(PATH_TO_CONFIG, 'r')
        self.CLIENT_ID = config.readline()
        self.CLIENT_SECRET = config.readline()
        config.close()
        self.imgur = pyimgur.Imgur(self.CLIENT_ID, self.CLIENT_SECRET)

    def upload_image(filename):
        return self.imgur.upload_image(filename).link

    def upload_album(title=DEFAULT_TITLE, image_list=[]):
        return self.imgur.create_album(title, image_list).link


class Picasa(ImageUploader):
    pass
    #How to create a client (from google example):
    # gd_client = gdata.photos.service.PhotosService()

    # Instead of using these lines I think we can just use Oauth2.0 to get in but I believe this will work
    # gd_client.email = '=change='     
    # gd_client.password = '=change='  

    # gd_client.source = 'api-sample-google-com'
    # gd_client.ProgrammaticLogin()

    #How to create a new album (from google example):
    # album = gd_client.InsertAlbum(title='New album', summary='This is an album')

    #How to upload a photo (from google example):
    # photo = gd_client.InsertPhotoSimple(album_url, 'New Photo', 
    #     'Uploaded using the API', filename, content_type='image/jpeg')

