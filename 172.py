f=open("17.txt")
k=0
for line in f:
    x=int(line)
    if x%3==0:
        k+=1
f.close()
f=open("17.txt")
max=-10000
k=0
pr=int(f.readline())
for line in f:
    sl=int(line)
    if (pr)%3==0 or (sl)%3==0:
        k+=1
        if pr+sl>max:
            max=pr+sl
    pr=sl
print(k,max)


           
