filenames = ["testdata2/testdata2" + str(i) for i in range(1, 101)]

for name in filenames:
    f = open(name, 'r').read().split('\n')[1 : - 1]
    f = [x.split('" "')[1].replace('"', '') for x in f]
    print(str(f).replace("'","").replace(", ",',')[1 : -1])


