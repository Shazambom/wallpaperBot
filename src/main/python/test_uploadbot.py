import unittest
import uploadbot
import os

def get_num_lines(text_file):
    counter = 0
    for line in text_file:
        counter += 1
    return counter

class MyTestCase(unittest.TestCase):

    def test_assign_directory_by_time(self):
        directory_string = uploadbot.assign_directory_by_time()
        self.assertRegexpMatches(directory_string, "Y20\d{2}-W\d{2}",
                                 "String " + directory_string + " doesn't match expected format")
        week = int(directory_string[-2:])
        self.assertLess(week, 53, "Week number is " + str(week) + " (greater than 52)")

    def test_get_image_filenames(self):
        TEST_PATH_BASE = 'Testing Helpers/Y2016-W20/'
        filenames = uploadbot.get_image_filenames(TEST_PATH_BASE)

        test_filenames = ['Testing Helpers/Y2016-W20/test_login-wallpaper.jpg',
                          'Testing Helpers/Y2016-W20/test_rambling wreck.jpg',
                          'Testing Helpers/Y2016-W20/test_MVrztby - Imgur.png',
                          'Testing Helpers/Y2016-W20/tester_Math2605Plot.png',
                          'Testing Helpers/Y2016-W20/tester_wallpaper.png',
                          'Testing Helpers/Y2016-W20/python-testing.pdf']

        for member in test_filenames:
            self.assertIn(member, filenames, str(member) + " was not in the gotten filenames")

    def test_write_link(self):
        test_link = "test_link"
        test_filename = 'test_file.txt'

        test_file = open(test_filename, 'w')
        uploadbot.write_link(test_link, test_file)
        test_file.close()
        test_file = open(test_filename, 'r')
        self.assertEqual(get_num_lines(test_file), 1, "File is not 1 line long. Lines: " + str(get_num_lines(test_file)))
        for line in test_file:
            self.assertEqual(line, test_link, "Expected: " + test_link + ", Actual: " + line)
        test_file.close()
        os.remove(test_filename)

    def test_get_thread_name(self):
        test_filename = "home/yash/Y2016-W21/threadname_imagename.png"
        thread_name = uploadbot.get_thread_name(test_filename)
        self.assertEqual(thread_name, "threadname", "Expected: threadname, Actual: " + thread_name)


if __name__ == '__main__':
    unittest.main()
