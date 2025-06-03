# PUCFlix - Trabalho Prático 2

## Objetivo

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

## Classes criadas

### `Actor (entities)`

- Estrutura de um ator: id, name
- Métodos principais: toByteArray, fromByteArray

### `ActorShow (entities)`

- Relaciona atores e séries: id, actorId, showId
- Métodos principais: toByteArray, fromByteArray

### `ActorsFile (model)`

- Gerencia atores no banco de dados
- Métodos principais: create, read, update, delete, readName (busca por nome usando índice invertido), readAll

### `ActorsShowsFile (model)`

- Gerencia o relacionamento entre atores e séries
- Métodos principais: create, read, delete, readShowActors, readActorShows, readAll

### `Seeder (util)`

- Popula o banco de dados com dados de exemplo
- Métodos principais: fillDB, fillShows, fillEpisodes, fillActorsAndRelations

---

## Checklist

- [x] As operações de inclusão, busca, alteração e exclusão de atores estão implementadas e funcionando corretamente? Sim
- [x] O relacionamento entre séries e atores foi implementado com árvores B+ e funciona corretamente, assegurando a consistência entre as duas entidades? Sim
- [x] É possível consultar quais são os atores de uma série? Sim
- [x] É posssível consultar quais são as séries de um ator? Sim
- [x] A remoção de séries remove os seus vínculos de atores? Sim
- [x] A inclusão de um ator em uma série em um episódio se limita aos atores existentes? Sim
- [x] A remoção de um ator checa se há alguma série vinculado a ele? Sim
- [x] O trabalho está funcionando corretamente? Sim
- [x] O trabalho está completo? Sim
- [x] O trabalho é original e não a cópia de um trabalho de outro grupo? Sim

---

## Relatório

Esse trabalho deu mais trabalho do que eu esperava. Mesmo com uma base já pronta, fazer o relacionamento N:N entre atores e séries funcionar direitinho não foi tão simples. Tive que quebrar a cabeça pra garantir que as buscas e os vínculos estivessem certos, sem dar erro ou ficar confuso. Fazer tudo sozinho acabou sendo puxado, talvez, com mais alguém na equipe, a gente tivesse achado soluções mais rápidas ou pensado em jeitos diferentes de resolver. No fim, deu tudo certo, mas foi uma boa oportunidade pra entender melhor como esse tipo de relacionamento funciona na prática.
