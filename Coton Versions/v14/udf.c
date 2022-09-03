/*
 Harness for C code generated from BPF.
 Nik Sultana, Illinois Tech, July 2022
*/

#include <stdio.h>

#include "resources.h"

FILE *file = NULL;

void
hook_accept (const unsigned char *packet) {
  printf("1\n");
}

void
hook_reject (const unsigned char *packet) {
  printf("0\n");
}

void
hook_start (void) {
  file = fopen("output.txt", "w");
  fprintf(file, "label,");
  fprintf(file, "packet_number,");
  fprintf(file, "accumulator,");
  fprintf(file, "packet_length,");
  fprintf(file, "idx_register,");
  for (int i = 0; i < SCRATCH_SIZE; i++) {
    fprintf(file, "scratch[%u],", i);
  }
  fprintf(file, "packet[]");
  fprintf(file, "\n");
}

void
hook_end (void) {
  fclose(file);
}

void
hook (const unsigned char *packet, const char* s) {
  if (0 == strcmp("dump",s)) {
    fprintf(file, "%s,", s);
    fprintf(file, "%u,", packet_number);
    fprintf(file, "%u,", accumulator);
    fprintf(file, "%u,", packet_length);
    fprintf(file, "%u,", idx_register);
    for (int i = 0; i < SCRATCH_SIZE; i++) {
      fprintf(file, "%u,", scratch[i]);
    }
    for (unsigned i = 0; i < packet_length; i++) {
      fprintf(file, "%02x", packet[i]);
    }
    fprintf(file, "\n");
  } else {
    fprintf(file, "%s\n", s);
  }
}

int
process_packet (unsigned char *packet) {
  #include "generated.c"
}
