// Variável global para controlar o status atual do filtro
let filtroStatusAtual = 'TODOS';

// CONTROLE DO MODAL DE MATRÍCULA
function abrirModalMatricula() {
    document.getElementById('formMatricula').reset();

    // Preenche a data de hoje por padrão
    const hoje = new Date().toISOString().split('T')[0];
    const inputData = document.querySelector('input[name="dataMatricula"]');
    if (inputData) {
        inputData.value = hoje;
    }

    document.getElementById('modalMatricula').classList.add('active');
}

function fecharModalMatricula() {
    document.getElementById('modalMatricula').classList.remove('active');
}

// FILTRO DE BUSCA POR NOME E STATUS
function filtrarMatriculas() {
    const inputBusca = document.getElementById('inputBusca');
    if (!inputBusca) return;
    
    const termo = inputBusca.value.toLowerCase().trim();
    const linhas = document.querySelectorAll('#tabelaMatriculas tr');

    linhas.forEach(linha => {
        const colunas = linha.querySelectorAll('td');
        if (colunas.length > 1) { // Ignora a linha de "Nenhuma matrícula encontrada"
            const nomeAluno = colunas[0].textContent.toLowerCase();
            const statusLinha = linha.getAttribute('data-status') || 'Ativa';

            const bateNome = nomeAluno.includes(termo);
            const bateStatus = (filtroStatusAtual === 'TODOS') || (statusLinha === filtroStatusAtual);

            if (bateNome && bateStatus) {
                linha.style.display = '';
            } else {
                linha.style.display = 'none';
            }
        }
    });
}

function filtrarStatus(status, elemento) {
    filtroStatusAtual = status;

    // Atualiza o estilo visual dos botões de filtro
    document.querySelectorAll('.btn-filter').forEach(btn => btn.classList.remove('active'));
    elemento.classList.add('active');

    filtrarMatriculas();
}

// LISTENERS GERAIS (Executados após o carregamento da página)
document.addEventListener('DOMContentLoaded', function () {
    // Fechar modal ao clicar na área escura de fundo
    const modal = document.getElementById('modalMatricula');
    if (modal) {
        modal.addEventListener('click', function (e) {
            if (e.target === this) {
                fecharModalMatricula();
            }
        });
    }
});