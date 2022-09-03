/*
Harness for C code generated from BPF.
Nik Sultana, Illinois Tech, July 2022
 */

#include <stdint.h>
#include <string.h>

#define MAX_PACKET_SIZE 1514
extern uint32_t packet_length;

extern uint32_t accumulator;
extern uint32_t idx_register;

#define SCRATCH_SIZE 16
extern uint32_t scratch[SCRATCH_SIZE];

extern uint32_t packet_number;
void step(const char *);
void dump(const unsigned char *packet);
#if defined(CT_TRACE)
#define STEP(X) step(X);
#define DUMP() dump(packet)
#else
#define STEP(X)
#define DUMP()
#endif /* defined(CT_TRACE) */
