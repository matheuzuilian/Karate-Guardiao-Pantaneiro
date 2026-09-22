// CONTROLE DO MODAL DE NOVA/EDITAR TURMA
function abrirModalTurma() {
    document.getElementById('formTurma').reset();
    document.getElementById('turmaId').value = '';
    document.querySelector('.modal-header h2').innerText = 'Nova Turma';
    
    // Desmarca todos os dias da semana
    document.querySelectorAll('.day-checkbox').forEach(cb => cb.checked = false);
    
    document.getElementById('modalTurma').classList.add('active');
}

function fecharModalTurma() {
    document.getElementById('modalTurma').classList.remove('active');
}

// Processa os checkboxes de dias da semana para o input hidden antes de salvar
function prepararDias() {
    const marcados = Array.from(document.querySelectorAll('.day-checkbox:checked')).map(cb => cb.value);
    const textoDias = marcados.length > 0 ? marcados.join(' · ') : 'A combinar';
    document.getElementById('diasSemanaInput').value = textoDias;
}

function editarTurma(idTurma) {
    fetch('/turmas/buscar/' + idTurma)
        .then(response => {
            if (!response.ok) throw new Error('Turma não encontrada.');
            return response.json();
        })
        .then(turma => {
            document.querySelector('.modal-header h2').innerText = 'Editar Turma';

            document.getElementById('turmaId').value = turma.idTurma;
            document.querySelector('input[name="nome"]').value = turma.nome || '';
            document.querySelector('input[name="horarioInicio"]').value = turma.horarioInicio || '';
            document.querySelector('input[name="horarioFim"]').value = turma.horarioFim || '';
            document.querySelector('input[name="idadeMinima"]').value = turma.idadeMinima || '';
            document.querySelector('input[name="idadeMaxima"]').value = turma.idadeMaxima || '';
            document.querySelector('input[name="vagasTotais"]').value = turma.vagasTotais || '';
            document.querySelector('input[name="instrutor"]').value = turma.instrutor || '';
            document.querySelector('select[name="status"]').value = turma.status || 'Ativa';

            // Desmarca todos os dias da semana antes de marcar os gravados
            document.querySelectorAll('.day-checkbox').forEach(cb => cb.checked = false);

            if (turma.diasSemana) {
                const diasArray = turma.diasSemana.split(' · ');
                diasArray.forEach(dia => {
                    const cb = Array.from(document.querySelectorAll('.day-checkbox')).find(c => c.value === dia);
                    if (cb) cb.checked = true;
                });
            }

            document.getElementById('modalTurma').classList.add('active');
        })
        .catch(error => console.error('Erro ao buscar dados da turma:', error));
}

// MÁSCARA AUTOMÁTICA PARA HORÁRIO (00:00)
function mascaraHorario(input) {
    let valor = input.value.replace(/\D/g, ""); // Remove tudo que não é dígito

    if (valor.length > 4) {
        valor = valor.slice(0, 4);
    }

    if (valor.length >= 3) {
        valor = valor.replace(/^(\d{2})(\d{1,2})$/, "$1:$2");
    }

    input.value = valor;
}

// --- FUNÇÕES DE ALUNOS DA TURMA ---
function carregarAlunosTurma(btn) {
    const idTurma = btn.getAttribute('data-id');
    const nomeTurma = btn.getAttribute('data-nome');

    document.getElementById('modalTurmaNomeTitle').innerText = nomeTurma;
    const tbody = document.getElementById('tabelaAlunosTurma');
    tbody.innerHTML = '<tr><td colspan="3" style="text-align: center; padding: 20px; color: var(--text-secondary);">Carregando alunos...</td></tr>';

    document.getElementById('modalVerAlunos').classList.add('active');

    fetch('/turmas/alunos/' + idTurma)
        .then(response => response.json())
        .then(matriculas => {
            tbody.innerHTML = '';

            if (matriculas.length === 0) {
                tbody.innerHTML = '<tr><td colspan="3" style="text-align: center; padding: 20px; color: var(--text-secondary);">Nenhum aluno matriculado nesta turma.</td></tr>';
                return;
            }

            matriculas.forEach(mat => {
                const tr = document.createElement('tr');

                let dataFormatada = '-';
                if (mat.dataMatricula) {
                    const partes = mat.dataMatricula.split('-');
                    if (partes.length === 3) dataFormatada = `${partes[2]}/${partes[1]}/${partes[0]}`;
                }

                const graduacao = mat.aluno && mat.aluno.graduacao ? mat.aluno.graduacao : 'Branca';
                const nomeAluno = mat.aluno ? mat.aluno.nome : 'Aluno não informado';

                tr.innerHTML = `
                    <td style="font-weight: 600; color: #F8FAFC;">${nomeAluno}</td>
                    <td>
                        <span class="badge-faixa badge-${graduacao.toLowerCase()}">${graduacao}</span>
                    </td>
                    <td style="color: #94A3B8;">${dataFormatada}</td>
                `;
                tbody.appendChild(tr);
            });
        })
        .catch(error => {
            console.error('Erro ao buscar alunos:', error);
            tbody.innerHTML = '<tr><td colspan="3" style="text-align: center; padding: 20px; color: #EF4444;">Erro ao carregar os alunos.</td></tr>';
        });
}

function fecharModalAlunos() {
    document.getElementById('modalVerAlunos').classList.remove('active');
}

// LISTENERS GERAIS (Executados após o carregamento da página)
document.addEventListener('DOMContentLoaded', function () {
    // Fechar modal de Turma ao clicar fora
    const modalTurma = document.getElementById('modalTurma');
    if (modalTurma) {
        modalTurma.addEventListener('click', function (e) {
            if (e.target === this) {
                fecharModalTurma();
            }
        });
    }

    // Fechar modal de Ver Alunos ao clicar fora
    const modalVerAlunos = document.getElementById('modalVerAlunos');
    if (modalVerAlunos) {
        modalVerAlunos.addEventListener('click', function (e) {
            if (e.target === this) {
                fecharModalAlunos();
            }
        });
    }
});