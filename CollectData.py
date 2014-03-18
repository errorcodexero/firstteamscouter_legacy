import os

#rootDir = "D:\\Profiles\\TomS\\Documents\\FIRST Robotics\\2014\\First Team Scouter\\tablet data files"
rootDir = "C:\\Users\\Tom\\Documents\\FIRST Robotics\\2014\\First Team Scouter\\tablet data files"

filesToParse = []
fileExt = '.csv'

entries = os.listdir(rootDir)
print entries

for entry in entries :
    path = rootDir + "\\" + entry
    if os.path.isdir(path) :
        csvFiles = [path + "\\" + f for f in os.listdir(path) if f.endswith(fileExt) and f.find('Copy') == -1]
        filesToParse += csvFiles
    elif os.path.isfile(path) :
        print "File: " + path
    else :
        print "/shrug: " + path

print "About to parse the following files: "
#print csvFiles

lines = []
filesParsed = 0
for csvFile in filesToParse :
    with open(csvFile) as f :
        lineCount = 0
        for line in f :
            if lineCount > 0 :
                lines.append(line.strip())
            lineCount += 1
    filesParsed += 1
    pathArray = csvFile.split("\\")
    path = "\\".join(pathArray[0:-1])
    savePath = path + "\\saved"
    saveFile = savePath + "\\" + pathArray[-1]
    if not os.path.isdir(savePath) :
       os.mkdir(savePath)
    try :
        os.rename(csvFile, saveFile)
    except WindowsError as e :
        print "Windows Error({0}): {1}".format(e.errno, e.strerror)
        print csvFile
        print saveFile

outFilePath = rootDir + "\\OregonCityMasterData.csv"
outFile = open(outFilePath, 'a')

print "Parsed " + str(filesParsed) + " csv files\n"
for line in lines:
    #print line
    #commas = line.count(',')
    #parts = line.split(",")
    #newLine = ','.join(parts[0:3])
    
    #if len(parts) > 50 :
    #    newLine += ',' + ';'.join(parts[3:commas-46])
    #    newLine += ',' + ','.join(parts[commas-46:])
    #else :
    #    newLine += ',' + ','.join(parts[3:])

    outFile.write(line.strip() + "\n")
outFile.close()






    
