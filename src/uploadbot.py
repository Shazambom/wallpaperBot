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


def write_link(image_link, album_image_file):
    album_image_file.write(image_link)
    album_image_file.write('\n')


def get_thread_name(filename):
    """
    takes string filename and returns string thread name
    thread name is everything before the last '_' and after the last '/'
    """
    return filename.split('_', 1)[0].rsplit('/', 1)[-1]
    # filename variable is the absolute path to the filename
    # we want everything in the filename before the underscore (done earlier and stored in thread)
    # but we want to remove the "/media/pi/pictures/" before it too
    # done now and stored in ablum_title


def upload_images_and_write_link_to_file(album, album_image_file, uploaded_count):
    for image_filename in album:
        if (uploaded_count < ImageUploader.UPLOAD_LIMIT):
            current_image_link = image_uploader.upload_image(image_filename)
            write_link(current_image_link, album_image_file)
            print ('uploaded')
            os.remove(image_filename)
            uploaded_count += 1
        else:
            print "upload limit exceeded!"
            return -1
    return uploaded_count


def get_same_thread_images(filenames, thread):
    """
    find other images from the same thread and add them to the album/album2 list
    :param album: first 150 images
    :param album2: next 150 images (if there are that many)
    :param filenames: list of all filenames
    :param thread: current thread
    :return: (album, album2)
    """
    album = []
    album2 = []
    for filename in filenames:
        thread2 = get_thread_name(filename)
        if (thread == thread2):
            if (len(album) > 149):
                album2.append(filename)
            else:
                album.append(filename)
    return album, album2


def upload():

    filenames = get_image_filenames(PATH_BASE)

    already_done = []
    for filename in filenames:
        thread = get_thread_name(filename) #returns everything before the first encountered underscore
        if thread not in already_done:
            # if there's too many images to fit in one album, put it in
            # a second album.
            # Ian guarantees that his code will not produce too many for
            # the second

            # album2 is empty unless there are more than 150 images in the same thread
            album, album2 = get_same_thread_images(filenames, thread)

            album_image_file = open(PATH_BASE + thread + '.txt', 'a')
            album2_image_file = open(PATH_BASE + thread + str(2) + '.txt', 'a')

            uploaded_count = upload_images_and_write_link_to_file(album, album_image_file, 0)

            if (len(album) > 149):
                uploaded_count = upload_images_and_write_link_to_file(album2, album2_image_file, uploaded_count)

            album_image_file.close()
            album2_image_file.close()
            already_done.append(thread)

            if uploaded_count == -1:
                print "force stop due to upload limit"
                return -1

upload()