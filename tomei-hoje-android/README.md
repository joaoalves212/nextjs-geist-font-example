# Tomei hoje? - Aplicativo Android de Rastreamento de Medicamentos

## 📱 Sobre o Aplicativo

"Tomei hoje?" é um aplicativo Android simples e eficiente para rastreamento diário de medicamentos. Com uma interface minimalista e tema escuro, permite que usuários criem uma lista personalizada de medicamentos e marquem diariamente quais foram tomados.

## ✨ Funcionalidades

- **Lista de Medicamentos**: Adicione e gerencie seus medicamentos pessoais
- **Rastreamento Diário**: Marque quais medicamentos foram tomados hoje
- **Persistência de Dados**: Dados salvos localmente usando Room Database
- **Interface Escura**: Tema escuro obrigatório para melhor experiência visual
- **Design Minimalista**: Interface limpa e intuitiva

## 🏗️ Arquitetura e Tecnologias

### Arquitetura
- **MVVM (Model-View-ViewModel)**: Separação clara de responsabilidades
- **Repository Pattern**: Camada de abstração para acesso aos dados

### Tecnologias Utilizadas
- **Linguagem**: Kotlin
- **Banco de Dados**: Room Database
- **Concorrência**: Kotlin Coroutines (Flow, StateFlow)
- **UI**: ViewBinding, RecyclerView, Material Components
- **Build System**: Gradle com Kotlin DSL
- **Gerenciamento de Dependências**: Version Catalog (libs.versions.toml)

### Especificações Técnicas
- **minSdk**: 24 (Android 7.0)
- **targetSdk**: 34
- **Tema**: Escuro obrigatório (#000000 fundo, elementos claros)

## 📁 Estrutura do Projeto

```
app/
├── src/main/
│   ├── java/com/example/tomeihoje/
│   │   ├── data/                    # Camada de dados (Room)
│   │   │   ├── Medication.kt        # Entidade do medicamento
│   │   │   ├── MedicationDao.kt     # Interface de acesso aos dados
│   │   │   └── AppDatabase.kt       # Configuração do banco Room
│   │   └── ui/                      # Camada de interface
│   │       ├── MainActivity.kt      # Activity principal
│   │       ├── MedicationAdapter.kt # Adapter do RecyclerView
│   │       ├── MedicationViewModel.kt # ViewModel (lógica de negócio)
│   │       └── MedicationViewModelFactory.kt # Factory do ViewModel
│   ├── res/
│   │   ├── layout/                  # Layouts XML
│   │   │   ├── activity_main.xml    # Layout da tela principal
│   │   │   └── list_item_medication.xml # Layout do item da lista
│   │   └── values/                  # Recursos (cores, strings, temas)
│   └── AndroidManifest.xml
└── build.gradle.kts                 # Configuração do módulo
```

## 🚀 Como Usar

### Pré-requisitos
- Android Studio Arctic Fox ou superior
- JDK 8 ou superior
- Android SDK com API 24+

### Instalação
1. Clone ou baixe o projeto
2. Abra no Android Studio
3. Sincronize o projeto (Gradle Sync)
4. Execute no dispositivo ou emulador

### Funcionalidades do App
1. **Adicionar Medicamento**: Toque no botão "+" para adicionar um novo medicamento
2. **Marcar como Tomado**: Use o checkbox ao lado de cada medicamento
3. **Excluir Medicamento**: Toque no ícone de lixeira para remover
4. **Rastreamento Diário**: O estado dos checkboxes é salvo por data

## 🔧 Detalhes Técnicos

### Persistência de Dados
- **Room Database**: Armazena a lista de medicamentos
- **SharedPreferences**: Salva o estado diário dos checkboxes (formato: `status_<id>_<data>`)

### Padrões Implementados
- **Singleton**: Para instância única do banco de dados
- **Observer Pattern**: StateFlow para atualizações reativas da UI
- **Factory Pattern**: Para criação do ViewModel com dependências

### Tratamento de Erros
- Validação de entrada do usuário
- Try-catch em operações de banco de dados
- Mensagens de erro amigáveis via Toast

## 🎨 Design

### Tema Escuro
- Fundo: `#000000` (preto)
- Texto: `#FFFFFF` (branco)
- Elementos secundários: `#E0E0E0` (cinza claro)
- Superfícies: `#333333` (cinza escuro)

### Componentes UI
- **Material Design 3**: Componentes modernos e acessíveis
- **RecyclerView**: Lista eficiente com DiffUtil
- **FloatingActionButton**: Ação principal destacada
- **AlertDialog**: Diálogos para entrada e confirmação

## 📱 Compatibilidade

- **Android 7.0 (API 24)** ou superior
- Suporte a diferentes tamanhos de tela
- Otimizado para dispositivos móveis

## 🔄 Ciclo de Vida dos Dados

1. **Inicialização**: Room cria/abre o banco de dados
2. **Carregamento**: ViewModel coleta dados via Flow
3. **Exibição**: UI observa StateFlow e atualiza automaticamente
4. **Interação**: Usuário adiciona/remove medicamentos ou marca como tomado
5. **Persistência**: Mudanças são salvas automaticamente

## 🧪 Testabilidade

A arquitetura MVVM facilita testes unitários:
- **ViewModel**: Lógica de negócio isolada
- **Repository/DAO**: Acesso aos dados mockável
- **UI**: Separada da lógica de negócio

## 📄 Licença

Este projeto foi desenvolvido como exemplo de aplicativo Android seguindo as melhores práticas modernas de desenvolvimento.

---

**Desenvolvido com ❤️ em Kotlin**
