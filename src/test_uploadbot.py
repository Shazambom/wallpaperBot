import unittest
import uploadbot

class MyTestCase(unittest.TestCase):

    def test_assign_directory_by_time(self):
        directory_string = uploadbot.assign_directory_by_time()
        self.assertRegexpMatches(directory_string, "Y20\d{2}-W\d{2}",
                                 "String " + directory_string + " doesn't match expected format")
        week = int(directory_string[-2:])
        self.assertLess(week, 53, "Week number is " + str(week) + " (greater than 52)")

    def test_get_image_filenames_in_directory(self):
        TEST_PATH_BASE = 'Testing Helpers/Y2016-W20/'
        filenames = uploadbot.get_image_filenamess_in_directory(TEST_PATH_BASE)

        test_filenames = ['Testing Helpers/Y2016-W20/test_login-wallpaper.jpg',
                          'Testing Helpers/Y2016-W20/test_rambling wreck.jpg',
                          'Testing Helpers/Y2016-W20/test_MVrztby - Imgur.png',
                          'Testing Helpers/Y2016-W20/tester_Math2605Plot.png',
                          'Testing Helpers/Y2016-W20/tester_wallpaper.png',
                          'Testing Helpers/Y2016-W20/python-testing.pdf']

        for member in test_filenames:
            self.assertIn(member, filenames, str(member) + " was not in the gotten filenames")

if __name__ == '__main__':
    unittest.main()
