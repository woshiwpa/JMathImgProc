# JMathImgProc
JAVA example to call the CoreMathImgProc lib using traditional JAVA. Also include regression test files. regression_test folder includes the math images for regression tests. regression_test_results folder including test result.

Developer can read source after importing this project into eclipse. In order to build this project, first make sure JMathImgProc is cloned in the same directory as CoreMathImgProc. Build CoreMathImgProc then. After CoreMathImgProc.jar is created, come back to the JMathImgProc folder and run gradle build . A JMathImgProc.jar will be created in bin folder.

In order to run regression test, navigate to the regression_test directory and run java -jar ../bin/JMathImgProc.jar 0 . The result will be output in the console. However, some unicode math characters may not be shown properly if the console doesn't support unicode. In this case, redirect the result to a text file, like java -jar ../bin/JMathImgProc.jar 0 > 1.txt , and then compare 1.txt with regression_test_results/regmath_sprint.txt. The two files should match.

Please note that the CoreMathImgProc is still under development. I don't have enough math formula images to train it. And I also don't have enough time to improve the accuracy. Contribution from others are very much welcomed.

If you want to use CoreMathImgProc lib in your application, please recognize it when your application starts. Also, please make contribution to the CoreMathImgProc lib during your development. Thanks.
