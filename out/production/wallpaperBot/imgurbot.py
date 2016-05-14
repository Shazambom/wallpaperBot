import glob       #to get filenames
import datetime   #to generate weekly folder paths
import pyimgur    #to upload files to imgur
import praw       #to post links to reddit/r/slashw
import os
import requests

# /media/UNTITLED/Wallpapers/

def assign_directory_by_time():
    path = 'Y'
    date = datetime.datetime.now()
    path = path + str(date.year)
    path = path + '-W' + str(date.isocalendar()[1])  #the week of the year
    return path

config = open('/home/pi/GitHub/wallpaperBot/config.txt', 'r')

CLIENT_ID = config.readline()
CLIENT_SECRET = config.readline()
config.close()
UPLOAD_LIMIT = 1000  #imgur limits daily uploads

PATH_BASE = '/media/UNTITLED/Wallpapers/' + assign_directory_by_time()
JPG_PATH = PATH_BASE + '/*.jpg'
JPEG_PATH = PATH_BASE + '/*.jpeg'
PNG_PATH = PATH_BASE + '/*.png'
APNG_PATH = PATH_BASE + '/*.apng'
BMP_PATH = PATH_BASE + '/*.bmp'
TIFF_PATH = PATH_BASE + '/*.tiff'
TIF_PATH = PATH_BASE + '/*.tif'
XCF_PATH = PATH_BASE + '/*.xcf'
PDF_PATH = PATH_BASE + '/*.pdf'

imgur = pyimgur.Imgur(CLIENT_ID, CLIENT_SECRET)


def upload_to_imgur():
    
    uploaded_count = 0

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


    image_link_files = []
    
    threads = []
    already_done = []
    for filename in filenames:
        thread = filename.split('_', 1)[0]  #returns everything before the first encountered underscore
        if (thread not in already_done):
            album = []
            album2 = []
            album_title = thread.rsplit('/', 1)[-1]  #removes everything before last '/'
            for filename2 in filenames:
                thread2 = filename2.split('_', 1)[0]
                if (thread == thread2):
                    if (len(album) > 149):
                        album2.append(filename2)
                    else:
                        album.append(filename2)
                        
            album_images = []
            album2_images = []
            album_image_file = open(thread + '.txt', 'a')
            album2_image_file = open(thread + str(2) + '.txt', 'a')
            
            for image_filename in album:
                if (uploaded_count < UPLOAD_LIMIT):
                    current_image = imgur.upload_image(image_filename)
                    album_images.append(current_image)
                    album_image_file.write(current_image.link)
                    album_image_file.write('\n')
                    print ('uploaded')
                    print (requests.get("https://api.imgur.com/3/credits").content)
                    uploaded_count += 1
                    os.remove(image_filename)
                
            if (len(album) > 149):
                for image_filename in album2:
                    if (uploaded_count < UPLOAD_LIMIT):
                        current_image = imgur.upload_image(image_filename)
                        album2_images.append(current_image)
                        album2_image_file.write(current_image.link)
                        album2_image_file.write('\n')
                        print ('uploaded')
                        print (requests.get("https://api.imgur.com/3/credits").content)
                        uploaded_count += 1
                        os.remove(image_filename)
            album_image_file.close()
            album2_image_file.close()
            already_done.append(thread)





upload_to_imgur()