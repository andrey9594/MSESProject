#encoding: UTF-8
#!/usr/bin/python

data = 'migr_imm2ctz.tsv'
output = 'immigr.txt'

def load_information():
    out = open(output, 'w')
    out.write('age,citizen,unit,sex,geo\time	2013 	2012 	2011 	2010 	2009 	2008    2007 	2006 	2005 	2004 	2003 	2002 	2001 	2000 	1999 	1998\n')
    with open(data, 'r') as f:
        for line in f:
            if line.find('IT') != -1 and line.find('TOTAL') != -1:
                out.write(line)
    out.close()

def main():
    load_information()

if __name__ == '__main__':
    main()
