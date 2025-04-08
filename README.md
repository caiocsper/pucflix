# PUCFlix - Trabalho Prático 1

## Objetivo
Praticar e melhorar a compreensão de como implementar estruturas de dados para administrar dados de arquivos persistentes através de um sistema de gerenciamento de séries e seus episódios.

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

### `Show (pucflix/entities)`
- Responsável por descrever a estrutura básica de uma série
- Atributos da série: id, name, summary, streamingOn, releaseYear
- Principais métodos: toByteArray, fromByteArray

### `Episode (pucflix/entities)`
- Responsável por descrever a estrutura básica de um episódio
- Atributos: id, showID, name, season, length, releaseDate
- Relacionamento com série via `showID`
- Principais métodos: toByteArray, fromByteArray

### `ShowsFile (pucflix/model)`
- Gerencia a relação dos nossos dados com o banco de dados diretamente (excluindo, atualizando, adicionar e recuperando dados)
- Extende da classe `Arquivo` que utiliza a Tabela Hash Extensível para indices diretos (id, endereço).
- Utiliza Árvore B+ tendo como chave pares `(name, id)`.
- Sobreescreve os métodos base herdados de `Arquivo` e acrescenta alterações respectivas à arvore b+.
- `readName` recupera todas as séries cadastradas na árvore b+ que o tem o name similar ao recebido por paramêtro.

### `EpisodesFile (pucflix/model)`
- Gerencia a relação dos nossos dados com o banco de dados diretamente (excluindo, atualizando, adicionar e recuperando dados)
- Extende da classe `Arquivo` que utiliza a Tabela Hash Extensível para indices diretos (id, endereço).
- Utiliza Árvore B+ tendo como chave pares `(name, id)` e `(id, id)`.
- A Árvore B+ de pares `(name, id)` serve principalmente para auxiliar na busca por nomes de episódio.
- A Árvore B+ de pares `(id, id)` serve principalmente para auxiliar na busca por episódios de uma série específica.
- Sobreescreve os métodos base herdados de `Arquivo` e acrescenta alterações respectivas à arvore b+.
- `readName` recupera todas os episódios cadastradas na árvore b+ que o tem o name similar ao recebido por paramêtro e filtra por aquele que possuem o `showId` igual ao `showId` da série sendo buscada atualmente.
- `isEmpty` serve para verificar se uma série possui episódios cadastrados.

### `ShowsMenu (pucflix/view)`
- Menu de opções para incluir, buscar, alterar e excluir séries
- Contém verificações para restrições como impedir exclusão de séries vinculadas a um ou mais episódios e impedir cadastramento de dados mal formatados.

### `EpisodesMenu (pucflix/view)`
- Menu de opções para incluir, buscar, alterar e excluir episódios
- Contém verificações para restrições como impedir cadastrar dados mal formatados em episódios e impedir a exclusão e leitura de episódios não relacionados à série selecionada.

### `Seeder (pucflix/utils)`
- Classe auxiliar para povoar o banco de dados.

### `Main (pucflix/)`
- Classe principal que administra o fluxo de execução do programa.

---


## Checklist da Atividade

- [x] As operações de inclusão, busca, alteração e exclusão de séries estão implementadas? Sim
- [x] As operações de inclusão, busca, alteração e exclusão de episódios por série estão ok? Sim
- [x] As operações usam CRUD genérico, Tabela Hash Extensível e Árvore B+? Sim
- [x] O atributo `idSerie` foi implementado como chave estrangeira na entidade `Episodio`? Sim
- [x] Há uma Árvore B+ para registrar o relacionamento 1:N entre episódios e séries? Sim
- [x] A visualização dos episódios por temporada está implementada? Sim
- [x] A exclusão da série verifica se há episódios vinculados? Sim
- [x] A inclusão de episódio se limita às séries existentes? Sim
- [x] O trabalho está funcionando corretamente? Sim
- [x] O trabalho está completo? Sim
- [x] O trabalho é original? Sim

---

## Relatório

Dado que a base do trabalho já continha muito código pronto, entender o código e adaptá-lo para funcionar da forma que eu queria foi relativamente simples. A maior dificuldade foi entender o funcionamento da árvore B+ e adaptá-la para trabalhar em conjunto com restrições de relacionamento entre dados, por exemplo no caso do método `readName` foi possível filtrar os dados retornados para apenas aqueles que tem relação com uma série N, mas para este resultado é necessário buscar todas os episódios por nome antes de realizar tal filtro. Assim creio que não consegui assimilar completamente a estrutura para trabalhar da forma que eu gostaria.

A fazer (Sujeito a mudança):
- Separar a view da controller. Atualmente a controller está totalmente integrada junto com a view, desrespeitando o padrão MVC.
- Organizar view e controller. Na versão atual, a view-controller (atualmente uma única estrutura) possui muitos código redundantes que podem ser melhorados simplesmente separando em métodos distintos.

---

## Participantes

- Caio César Pereira
- Arthur Crossy Reis Mendes
