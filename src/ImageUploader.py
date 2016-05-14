import pyimgur    #to upload files to imgur

PATH_TO_CONFIG = '/home/pi/GitHub/wallpaperBot/config.txt'
DEFAULT_TITLE = "/R/SLASHW"


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
        self.UPLOAD_LIMIT = 1000  #imgur limits daily uploads
        self.imgur = pyimgur.Imgur(self.CLIENT_ID, self.CLIENT_SECRET)

    def upload_image(filename):
        return self.imgur.upload_image(filename)

    def upload_album(title=DEFAULT_TITLE, image_list=[]):
        return self.imgur.create_album(title, image_list)
