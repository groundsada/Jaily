# Nik Sultana, Illinois Tech, July 2022
# Mohammad Firas Sada, Illinois Tech, July 2022

.PHONY: clean deepclean info

ifdef DEBUGGING
DEBUGGING=-O0 -g -include stdbool.h -DCT_TRACE
else
DEBUGGING=-O3
endif

# from https://stackoverflow.com/questions/154630/recommended-gcc-warning-options-for-c
CFLAGS+=-Wall -Wextra -Wformat=2 -Wswitch-default -Wcast-align -Wpointer-arith \
    -Wbad-function-cast -Wstrict-prototypes -Winline -Wundef -Wnested-externs \
    -Wcast-qual -Wshadow -Wwrite-strings -Wconversion -Wunreachable-code \
    -Wstrict-aliasing=2 -fno-common -fstrict-aliasing -fPIE \
    -std=gnu99 -pedantic \
    -D_BSD_SOURCE \
    $(DEBUGGING)

LFLAGS+=-lpcap

TGT=harness

$(TGT): udf.c generated.c harness.o
	$(CC) $(CFLAGS) udf.c harness.o $(LFLAGS) -o $@

aux.o: aux.c
	$(CC) $(CFLAGS) aux.c -c
$(TGT).o: harness.c resources.h aux.o
	$(CC) $(CFLAGS) harness.c -c -o pre-harness.o
	ld -r pre-harness.o aux.o -o harness.o

info:
	uname -a
	$(CC) -v

clean:
	rm -f $(TGT)

deepclean: clean
	rm -f $(TGT).o aux.o
