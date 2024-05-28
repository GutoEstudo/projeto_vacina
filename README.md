As tabelas não são criadas automaticamente, para facilitar segue os comandos SQL: 

CREATE TABLE IF NOT EXISTS `projeto_vacina`.`Localizacao` (
 `cod_ibge` INT NOT NULL,
 `estado` VARCHAR(45) NOT NULL,
 `nome_cidade` VARCHAR(45) NOT NULL,
 `regiao` VARCHAR(45) NOT NULL,
 PRIMARY KEY (`cod_ibge`))

 CREATE TABLE IF NOT EXISTS `projeto_vacina`.`Pessoa` (
 `cpf` VARCHAR(11) NOT NULL,
 `nome` VARCHAR(45) NOT NULL,
 `idade` INT NOT NULL,
 `escolaridade` VARCHAR(45) NOT NULL,
 `localizacao` INT NULL,
 PRIMARY KEY (`cpf`),
 INDEX `localizacao_pessoa_fk_idx` (`localizacao` ASC) VISIBLE,
 CONSTRAINT `localizacao_pessoa_fk`
 FOREIGN KEY (`localizacao`)
REFERENCES `projeto_vacina`.`Localizacao` (`cod_ibge`)
 ON DELETE NO ACTION
 ON UPDATE NO ACTION)

 CREATE TABLE IF NOT EXISTS `projeto_vacina`.`Vacina` (
 `id_vacina` INT NOT NULL AUTO_INCREMENT,
 `doenca` VARCHAR(45) NULL,
 `num_lote` INT NOT NULL,
 `qtd_doses` INT NOT NULL,
 PRIMARY KEY (`id_vacina`))

 CREATE TABLE IF NOT EXISTS `projeto_vacina`.`Recebe` (
 `id_vacina` INT NOT NULL,
 `cpf` VARCHAR(11) NOT NULL,
 `localizacao` INT NOT NULL,
 `nome_profissional` VARCHAR(45) NULL,
 `data` DATE NOT NULL,
 INDEX `vacina_fk_idx` (`id_vacina` ASC) VISIBLE,
 INDEX `cpf_fk_idx` (`cpf` ASC) VISIBLE,
 INDEX `localizacao_fk_idx` (`localizacao` ASC) VISIBLE,
 PRIMARY KEY (`id_vacina`, `cpf`, `localizacao`),
 CONSTRAINT `vacina_fk`
 FOREIGN KEY (`id_vacina`)
 REFERENCES `projeto_vacina`.`Vacina` (`id_vacina`)
 ONDELETENOACTION
 ONUPDATENOACTION,
 CONSTRAINT `cpf_fk`
 FOREIGN KEY (`cpf`)
 REFERENCES `projeto_vacina`.`Pessoa` (`cpf`)
 ONDELETENOACTION
 ONUPDATENOACTION,
 CONSTRAINT `localizacao_fk`
 FOREIGN KEY (`localizacao`)
 REFERENCES `projeto_vacina`.`Localizacao` (`cod_ibge`)
 ONDELETENOACTION
 ONUPDATENOACTION)

 Temos a entidade vacina, que possui
 o número do lote que identifica o lote na qual ela foi distribuída, para caso ocorra um
 problema relacionado ao lote, possamos saber quem foi afetado. Outro atributo da
 entidade vacina é seu id, que é gerado automaticamente ao ser inserida uma nova
 vacina no banco de dados e o nome da doença que a vacina ajuda a curar. Já na
 entidade pessoa, temos alguns atributos básicos para saber informações sobre a
 pessoa, como o nome e a idade, além do CPF, que é único para cada pessoa. Além
 disso, temos a escolaridade que é necessária a fim de se fazer pesquisas
 relacionando saúde à escolaridade, além de envolver questões sócio-econômicas.
 Temos também a entidade localização, que conta com o código ibge da cidade
 como identificador único, o nome da cidade, do estado e da região. Existe uma
 relação entre a entidade pessoa e a entidade localização, pois é necessário
 identificar o local de origem da pessoa vacinada a fim de se analisar onde está o
 maior foco de vacina, os locais com menos vacinados para que se possa investir
 mais em saúde nessa localização, além de outras medidas sociais. Existe uma outra
 tabela que representa uma relação entre essas três entidades mencionadas, que é
 uma tabela de histórico de recebimento de vacina, que conta com os identificadores
 únicos das outras tabelas (local, pessoa, id da vacina).
