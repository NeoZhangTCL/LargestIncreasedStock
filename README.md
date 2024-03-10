# How I trackle this problem

When I first got this hack question, I wondered lots of questions, like only a few companies do a infinate time coding challenge, and what our company hope to know from this challenge.

Later, I decided to treat it as a real problem I will face in our company. I will resolve the problem with some common technologies and libaraies I usually use when handling my work. Carefully decouple my codes because I do not know how it will used by my collegues (my interviewers). Meanwhile, I will try to make its performance in a best level to make sure it meet our requirement.

## Question

Please tell me the stock with the largest absolute increase from its first recording to its last recording. This is complicated by the file being unsorted, and having lots of nullvalues, with non-standard null entries (unknown, NA, N/A, UNKOWN, etc)

Please optimize your code to get the best performance.

## Examples

| Name | Date | Notes | Value | Change |
| --- | --- | --- | --- |--- |
| IQZ | 2015-7-8 | notes | 656.36 | INCREASED |
| DLV | 2015-8-8 | notes | 173.35 | INCREASED |
| DLV | 2015-10-4 | notes | 231.67 | INCREASEL |
| DLV | 2015-9-7 | notes | 209.57 | DECREASED |
| IQZ | 2015-9-7 | notes | 641.23 | DECREASED |
| IQZ | 2015-10-4 | notes | 657.32 | INCREASED |
| DLV | 2015-8-18 | notes | 233.43 | INCREASED |
| DLV | 2015-9-15 | notes | 158.73 | DECREASED |
| IQZ | 2015-10-8 | notes | 537.53 | DECREASED |
| IQZ | 2015-10-6 | notes | Invalid | UNKNOWN |

Print: Company:DLV Increased:58.320000

| Name | Date | Notes | Value | Change |
| --- | --- | --- | --- |--- |
| IQZ | 2015-7-8 | notes | 656.36 | DECREASED |
| DLV | 2015-8-8 | notes | 773,35 | DECREASED |
| DLV | 2015-10-4 | notes | 231.67 | DECREASED |
| DLV | 2015-9-7 | notes | 299,57 | DECREASED |

Print: nil

## Analysis

### Answer Required

First I am trying to find out what this question actually hope me to answer. By these example and the input values, here is what I got:

1. First we need to find out in the given data set, each company's record tuples with first and last date.
2. Calculate the positive gap between the two date values.
3. Return the company with largest positive gap.

### Corner Cases

Of course, as a simulation of a real world problem, there are lots of corner cases we need consider:

1. If all the company has a decreasing value from the first to last date, return `nil` (as the second example).
2. If there is a company with only one valid record, skip it, as there is no increasing
3. If date or value is invalid, skip them as it is uncalculatable.

### Some Assumptions

1. Only the value in a column will be invalied, the releation in a row should always be correct. Such as the `Change` column always right, otherwise I will waste too much time on data valiation. Usually if the `Value` record is valid, the `Change` will remains valid also. After viewing the `values.csv`, I think I can still use this assumption.
2. If the first or last date tuple is invalid, I will use the second frist or last tuple to calculate the result, otherwise I have to skip the whole company's data which we usually care in a real world.

### Solution

1. Treat this question a noraml question, go through all lines, for each company record the first and last record, calculate the gap, and update the largest incrased if the gap is greater than the maintained largest increased value.
2. Later we notice that this problem is offered a large size data set in csv like 5000 lines, actually in a real world, this file will be way larger than now, to load all the data and calculate the result as fast as possible. We can use some multi-threading way to handle this problem parallelly.
3. If there is `N` line in this file, and `M` threads we used, the read will takes `O(N/M)`, and let's assume there is `K` companies in this file. The time complexity is `O(Math.max(N/M, K))`. The space complexity is `O(K)`.

## Run the codes

1. (optional) Install `gradle` according to: https://gradle.org/install/
2. `cd largest-increased-stock`
3. run `./gradlew build`
4. run `./gradlew run`
