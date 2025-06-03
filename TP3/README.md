# PUCFlix - Trabalho Prático 3

## O que o trabalho faz?

Este projeto implementa um sistema de gerenciamento de séries, episódios e atores.

---

## Participantes

- Caio César Pereira
- Arthur Crossy Reis Mendes

---

## Como compilar e executar

1. Clone o repositório:

```bash
git clone git@github.com:caiocsper/pucflix.git
cd pucflix
```

2. Compile o projeto com javac:

```bash
javac Main.java
```

3. Execute o projeto:

```bash
java Main
```

---

## Classes criadas e principais métodos

### `InvertedIndex (model)`

- Implementa o índice invertido (ListaInvertida) para buscas por palavras em títulos de séries, episódios e nomes de atores
- Métodos principais: index, remove, update, search

### `TextUtils (util)`

- Utilitário para normalização de texto, remoção de acentuação e stop words
- Métodos principais: normalize, tokenize

---

## Checklist

- [x] O índice invertido com os termos dos títulos das séries foi criado usando a classe ListaInvertida? Sim
- [x] O índice invertido com os termos dos títulos dos episódios foi criado usando a classe ListaInvertida? Sim
- [x] O índice invertido com os termos dos nomes dos atores foi criado usando a classe ListaInvertida? Sim
- [x] É possível buscar séries por palavras usando o índice invertido? Sim
- [x] É possível buscar episódios por palavras usando o índice invertido? Sim
- [x] É possível buscar atores por palavras usando o índice invertido? Sim
- [x] O trabalho está completo? Sim
- [x] O trabalho é original e não a cópia de um trabalho de um colega? Sim

---

## Relatório

Essa parte do projeto foi mais tranquila de lidar, principalmente porque o código base já estava bem encaminhado. A maior dor de cabeça foi mesmo entender como a lista invertida com TFxIDF se encaixava nas buscas, especialmente ajustando os detalhes com stop words e acentuação. Nada impossível, mas exigiu um pouco de paciência e testes até tudo ficar redondinho.
