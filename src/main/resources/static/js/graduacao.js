// ORDEM DAS FAIXAS DE KARATÊ
const ordemFaixas = {
    'Branca': 'Amarela',
    'Amarela': 'Laranja',
    'Laranja': 'Verde',
    'Verde': 'Roxa',
    'Roxa': 'Marrom',
    'Marrom': 'Preta',
    'Preta': 'Preta'
};

function atualizarProximaFaixaSugerida() {
    const selectAluno = document.getElementById('selectAlunoModal');
    if (!selectAluno) return;

    const optionSelecionada = selectAluno.options[selectAluno.selectedIndex];
    if (optionSelecionada) {
        const faixaAtual = optionSelecionada.getAttribute('data-faixa-atual') || 'Branca';
        const proximaFaixa = ordemFaixas[faixaAtual] || 'Amarela';

        const selectFaixa = document.getElementById('selectFaixaNova');
        if (selectFaixa) {
            selectFaixa.value = proximaFaixa;
        }
    }
}

// CONTROLE DO AUTOCOMPLETE / DROPDOWN DE ALUNOS
function abrirDropdownAlunos() {
    document.getElementById('dropdownAlunos').style.display = 'block';
}

function filtrarDropdownAlunos() {
    const termo = document.getElementById('buscaAlunoSelect').value.toLowerCase().trim();
    const itens = document.querySelectorAll('#dropdownAlunos li');
    const dropdown = document.getElementById('dropdownAlunos');

    dropdown.style.display = 'block';

    itens.forEach(item => {
        const nome = item.getAttribute('data-nome').toLowerCase();
        if (nome.includes(termo)) {
            item.style.display = 'block';
        } else {
            item.style.display = 'none';
        }
    });
}

function selecionarAluno(idAluno) {
    window.location.href = '/graduacoes?alunoId=' + idAluno;
}

// CONTROLE DO MODAL DE NOVA GRADUAÇÃO
function abrirModalGraduacao() {
    // Ajusta a faixa sugerida ao abrir a janela
    atualizarProximaFaixaSugerida();
    document.getElementById('modalGraduacao').classList.add('active');
}

function fecharModalGraduacao() {
    document.getElementById('modalGraduacao').classList.remove('active');
}

// LISTENERS GERAIS (Executados após o carregamento da página)
document.addEventListener('DOMContentLoaded', function () {

    // Fechar o dropdown de alunos ao clicar fora
    document.addEventListener('click', function (e) {
        const inputBusca = document.getElementById('buscaAlunoSelect');
        const dropdown = document.getElementById('dropdownAlunos');
        if (inputBusca && dropdown && !inputBusca.contains(e.target) && !dropdown.contains(e.target)) {
            dropdown.style.display = 'none';
        }
    });

    // Fechar modal de graduação ao clicar na área escura (fora do card)
    const modalGraduacao = document.getElementById('modalGraduacao');
    if (modalGraduacao) {
        modalGraduacao.addEventListener('click', function (e) {
            if (e.target === this) {
                fecharModalGraduacao();
            }
        });
    }

});