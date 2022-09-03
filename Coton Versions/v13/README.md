```
$ ./coton.native --convert_symbolic_to_numeric tests/protocol_is_TCP.symbolic
12
40 0 0 12
21 0 5 34525
48 0 0 20
21 6 0 6
21 0 6 44
48 0 0 54
21 3 4 6
21 0 3 2048
48 0 0 23
21 0 1 6
6 0 0 262144
6 0 0 0
```

```
$ ./coton.native --convert_numeric_to_symbolic tests/protocol_is_TCP.numeric
ldh [12]
jeq #34525 jt 2 jf 7
ldb [20]
jeq #6 jt 10 jf 4
jeq #44 jt 5 jf 11
ldb [54]
jeq #6 jt 10 jf 11
jeq #2048 jt 8 jf 11
ldb [23]
jeq #6 jt 10 jf 11
ret #262144
ret #0
```

```
$ ./coton.native --convert_symbolic_to_C tests/protocol_is_TCP.symbolic
l0: accumulator = (packet[0xc] << 0x8) | packet[0xd]; // ldh [0xc]
l1: if (accumulator == 0x86dd) {
  goto l4;
} else {
  goto l9;
} // jeq #0x86dd jt 0x2 jf 0x7
l2: accumulator = packet[0x14]; // ldb [0x14]
l3: if (accumulator == 0x6) {
  goto l14;
} else {
  goto l8;
} // jeq #0x6 jt 0xa jf 0x4
l4: if (accumulator == 0x2c) {
  goto l10;
} else {
  goto l16;
} // jeq #0x2c jt 0x5 jf 0xb
l5: accumulator = packet[0x36]; // ldb [0x36]
l6: if (accumulator == 0x6) {
  goto l17;
} else {
  goto l18;
} // jeq #0x6 jt 0xa jf 0xb
l7: if (accumulator == 0x800) {
  goto l16;
} else {
  goto l19;
} // jeq #0x800 jt 0x8 jf 0xb
l8: accumulator = packet[0x17]; // ldb [0x17]
l9: if (accumulator == 0x6) {
  goto l20;
} else {
  goto l21;
} // jeq #0x6 jt 0xa jf 0xb
l10: return (0x40000); // ret #0x40000
l11: return (0x0); // ret #0x0
```

```
$ ./coton.native --convert_symbolic_to_C tests/protocol_is_TCP.symbolic > generated.c
$ make harness
$ ./harness -i fuzz-2006-08-23-6489.pcap
```
