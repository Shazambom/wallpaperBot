import glob       #to get filenames
import datetime   #to generate weekly folder paths
import os
import ImageUploader

def assign_directory_by_time():
    path = 'Y'
    date = datetime.datetime.now()
    path = path + str(date.year)
    path = path + '-W' + str(date.isocalendar()[1])  #the week of the year
    return path


image_uploader = ImageUploader.Imgur()

# PATH_BASE = '/media/UNTITLED/Wallpapers/'
PATH_BASE = '/home/yash/code/Ian4chanProject/Images/'
PATH_BASE += assign_directory_by_time() + '/'

def get_image_filenamess_in_directory(path_base):
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

def upload_and_write_link(image_uploader, image_filename, album_image_file):
    current_image = image_uploader.upload_image(image_filename)
    album_image_file.write(current_image)
    album_image_file.write('\n')
    print ('uploaded')
    os.remove(image_filename)


def upload():

    uploaded_count = 0

    filenames = get_image_filenamess_in_directory(PATH_BASE)

    already_done = []
    for filename in filenames:
        thread = filename.split('_', 1)[0]  #returns everything before the first encountered underscore
        if (thread not in already_done):
            album = []
            album2 = []

            #filename variable is the absolute path to the filename
            #we want everything in the filename before the underscore (done earlier and stored in thread)
            #but we want to remove the "/media/pi/pictures/" before it too
            #done now and stored in ablum_title
            album_title = thread.rsplit('/', 1)[-1]  #removes everything before last '/'

            for filename2 in filenames:
                thread2 = filename2.split('_', 1)[0]
                if (thread == thread2):
                    #if there's too many images to fit in one album, put it in
                    #a second album.
                    #Ian guarantees that his code will not produce too many for
                    #the second
                    if (len(album) > 149):
                        album2.append(filename2)
                    else:
                        album.append(filename2)

            album_image_file = open(thread + '.txt', 'a')
            album2_image_file = open(thread + str(2) + '.txt', 'a')

            for image_filename in album:
                if (uploaded_count < ImageUploader.UPLOAD_LIMIT):
                    upload_and_write_link(image_uploader, image_filename, album_image_file)
                    uploaded_count += 1


            if (len(album) > 149):
                for image_filename in album2:
                    if (uploaded_count < ImageUploader.UPLOAD_LIMIT):
                        upload_and_write_link(image_uploader, image_filename, album2_image_file)
                        uploaded_count += 1

            album_image_file.close()
            album2_image_file.close()
            already_done.append(thread)
