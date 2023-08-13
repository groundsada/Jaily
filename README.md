# Jaily - Just Another Interpreter Like Yours

Jaily is a transpiler written in Java that converts natural language input (NRL) into BPF rules, allowing for the application of network packet filters. The transpiler consists of a parser and a code generator, capable of handling stateful rules and manual packet manipulation using BPF. It has undergone extensive evaluation using multiple PCAP files and NRL rule files. A toolchain in C has been utilized to apply the generated BPF rules. This transpiler offers a novel solution for configuring and securing distributed systems using natural language, and its effectiveness has been rigorously tested.

## Features

- Transpiles natural language input (NRL) into BPF rules for network packet filtering.
- Includes a parser and code generator for seamless transformation.
- Supports stateful rules and manual packet manipulation using BPF.
- Evaluation performed using various PCAP files and NRL rule files.
- Toolchain in C provided for applying generated BPF rules.
- Enables configuration and security of distributed systems through natural language.

## Example Transpilation

Transpiling NRL rules to BPF involves the following steps:

1. **User Input**: Write a set of rules in NRL, using natural language statements to describe the desired network system behavior. For instance:

`If Packet.TCPFlow then IPSrc must be ‘192.168.1.1’`

2. **Transpilation Process**: The transpiler parses the NRL rule and generates code to check packet protocol and destination port, determining if it's part of an established flow or a new connection attempt.

3. **BPF Code Generation**: Based on the parsed information, the transpiler generates BPF code that enforces the desired behavior. This may involve tracking TCP flow state and other packet attributes.

An example BPF code snippet generated for the provided NRL rule:

```c
rule1l1: ldh [12]
rule1l2: jeq #2048 , rule1l3 , rule1l6
rule1l3: ldb [23]
// Additional BPF code for managing flow state and behavior enforcement
```

By transpiling NRL rules into BPF, complex packet handling behavior within TCP flows can be implemented, while still allowing non-technical users to express rules in natural language.

## License

Jaily is distributed under the [MIT License](LICENSE.md).

## Paper

For more in-depth information, you can refer to the research paper: [Link to Paper](paper.pdf)

