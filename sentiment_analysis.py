# Shaylan Pratt Assignment 3 - This function takes in a file of tweets and analyses the tweets to determine which region of the
# USA is happiest
def compute_tweets(tweetsTxtName, keyWordsTxtName):
    ten = []
    one = []
    seven = []
    five = []
    emptyList = []
    space = " "
    keyWords = 0
    sentiment = 0
    easternTweets = 0
    centralTweets = 0
    mountainTweets = 0
    pacificTweets = 0
    easternKey = 0
    centralKey = 0
    mountainKey = 0
    pacificKey = 0
    easternScore = 0
    centralScore = 0
    mountainScore = 0
    pacificScore = 0
    tweetsNew = []
    tweets = []
    punc = "!@#$%\?^&*<>_-.;:',/|\n"

    try:
        keyWordsTxt = open(keyWordsTxtName,"r",encoding="utf-8")
        for line in keyWordsTxt:             # this sorts through the key words and puts them into lists based on their value
            entries = line.split(",")
            entries[1] = int(entries[1].rstrip("\n"))
            if entries[1] == 10:
                ten.append(entries[0])
            if entries[1] == 1:
                one.append(entries[0])
            if entries[1] == 7:
                seven.append(entries[0])
            if entries[1] == 5:
                five.append(entries[0])
        keyWordsTxt.close()
    except FileNotFoundError:     # if the file is not found, it will return an empty list
        return []
        quit()

    try:
        tweetsTxt = open(tweetsTxtName,"r",encoding="utf-8")
        class Tweet:
            def __init__(self, long, lat, timezone, keywords, score):     # this creates a class for the tweets and it stores the data for each tweet
                self.long = long
                self.lat = lat
                self.timezone = timezone
                self.keywords = keywords
                self.score = score

        for line in tweetsTxt:
            sentiment = 0
            keyWords = 0
            tweetsNew = line.split(space)
            tweetsNew = [x.lower() for x in tweetsNew]  # makes words lower case
            tweetsNew = [i.strip(punc) for i in tweetsNew]  # takes out punctuation
            coord = line.split("]")                          # these next 4 lines get the coordinates
            coordinates = str(coord).split(",")
            lat = float(coordinates[0].replace("['[",""))
            long = float(coordinates[1].strip("'"))         # next it determines the time zone
            if lat >= 24.660845 and lat <= 49.189787 and long >= -87.518395 and long <= -67.444574:
                timeZone = "eastern"
            elif lat >= 24.660845 and lat <= 49.189787 and long >= -115.236428 and long <= -101.998892:
                timeZone = "mountain"
            elif lat >= 24.660845 and lat <= 49.189787 and long >= -101.998892 and long <= -87.518395:
                timeZone = "central"
            elif lat >= 24.660845 and lat <= 49.189787 and long >= -125.242264 and long <= -115.236428:
                timeZone = "pacific"
            else:
                timeZone = "outside region"

            for word in tweetsNew:            # this finds the keywords in the tweet and determines the score
                if word in ten:
                    sentiment += 10
                    keyWords += 1
                if word in seven:
                    sentiment += 7
                    keyWords += 1
                if word in five:
                    sentiment += 5
                    keyWords += 1
                if word in one:
                    sentiment += 1
                    keyWords += 1

            tweets.append(Tweet(long, lat, timeZone, keyWords, sentiment))  # here the class is being appended to a list

        for item in tweets:                      # this loop counts all the tweets in each region, the keyword tweets, and the score
            if item.timezone == "eastern":
                easternTweets += 1
                easternScore = easternScore + item.score
                if item.keywords > 0:
                    easternKey += 1
            if item.timezone == "central":
                centralTweets += 1
                centralScore = centralScore + item.score
                if item.keywords > 0:
                    centralKey += 1
            if item.timezone == "mountain":
                mountainTweets += 1
                mountainScore = mountainScore + item.score
                if item.keywords > 0:
                    mountainKey += 1
            if item.timezone == "pacific":
                pacificTweets += 1
                pacificScore = pacificScore + item.score
                if item.keywords > 0:
                    pacificKey += 1

        happinessEastern = easternScore/easternKey        # here it determines the total happiness score for each region
        happinessCentral = centralScore/centralKey
        happinessMountain = mountainScore/mountainKey
        happinessPacific = pacificScore/pacificKey

        easternTuple = (happinessEastern, easternKey, easternTweets)    # here is putting the score, keyword tweets, and total tweets in a tuple
        centralTuple = (happinessCentral, centralKey, centralTweets)
        mountainTuple = (happinessMountain, mountainKey, mountainTweets)
        pacificTuple = (happinessPacific, pacificKey, pacificTweets)
        finalList = [easternTuple, centralTuple, mountainTuple, pacificTuple]

    except FileNotFoundError:
        return []
        quit()

    return finalList



