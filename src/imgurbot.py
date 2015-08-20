import glob       #to get filenames
import datetime   #to generate weekly folder paths
import pyimgur    #to upload files to imgur
import praw       #to post links to reddit/r/slashw

# /media/UNTITLED/Wallpapers/

def assign_directory_by_time():
    path = 'Y'
    date = datetime.datetime.now()
    path = path + str(date.year)
    path = path + '-W' + str(date.isocalendar()[1])  #the week of the year
    return path

CLIENT_ID = '2e6582b4e4109df'
CLIENT_SECRET = '9a0e29fb2220d772a81a56a0d3a4f9fee9d8b29b'

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
                current_image = imgur.upload_image(image_filename)
                album_images.append(current_image)
                album_image_file.write(current_image.link)
                album_image_file.write('\n')
                print ('uploaded')
            if (len(album) > 149):
                for image_filename in album2:
                    current_image = imgur.upload_image(image_filename)
                    album2_images.append(current_image)
                    album2_image_file.write(current_image.link)
                    album2_image_file.write('\n')
                    print ('uploaded')
            album_image_file.close()
            album2_image_file.close()
            already_done.append(thread)





upload_to_imgur()
