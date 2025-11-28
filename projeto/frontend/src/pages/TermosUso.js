import React from 'react';
import { Link } from 'react-router-dom';
import { ArrowLeft, FileText, AlertTriangle, CheckCircle, XCircle, Shield } from 'lucide-react';

function TermosUso() {
  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-4xl mx-auto px-4 py-8">
        <Link
          to="/login"
          className="inline-flex items-center text-blue-600 hover:text-blue-800 mb-6"
        >
          <ArrowLeft className="w-4 h-4 mr-2" />
          Voltar
        </Link>

        <div className="bg-white rounded-lg shadow-sm p-8">
          <div className="flex items-center mb-6">
            <FileText className="w-8 h-8 text-blue-600 mr-3" />
            <h1 className="text-3xl font-bold text-gray-900">Termos de Uso</h1>
          </div>

          <p className="text-gray-600 mb-8">
            <strong>Última atualização:</strong> {new Date().toLocaleDateString('pt-BR', { 
              year: 'numeric', 
              month: 'long', 
              day: 'numeric' 
            })}
          </p>

          <div className="prose max-w-none">
            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4 flex items-center">
                <FileText className="w-6 h-6 mr-2 text-blue-600" />
                1. Aceitação dos Termos
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Ao acessar e utilizar o sistema <strong>CRM Shot Fair Brasil</strong> ("Sistema", 
                "Plataforma" ou "Serviço"), você concorda em cumprir e estar vinculado a estes Termos 
                de Uso. Se você não concordar com qualquer parte destes termos, não deve utilizar o Sistema.
              </p>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4 flex items-center">
                <Shield className="w-6 h-6 mr-2 text-blue-600" />
                2. Descrição do Serviço
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                O CRM Shot Fair Brasil é uma plataforma de gestão de relacionamento com clientes (CRM) 
                que permite:
              </p>
              <ul className="list-disc pl-6 text-gray-700 space-y-2 mb-4">
                <li>Cadastro e gestão de expositores e clientes</li>
                <li>Acompanhamento de oportunidades de vendas</li>
                <li>Registro de interações e atividades comerciais</li>
                <li>Geração de relatórios e dashboards analíticos</li>
                <li>Gestão de agenda e follow-ups</li>
                <li>Assistente CRM para consultas rápidas</li>
              </ul>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4 flex items-center">
                <CheckCircle className="w-6 h-6 mr-2 text-blue-600" />
                3. Cadastro e Conta de Usuário
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Para utilizar o Sistema, você precisa:
              </p>
              <ul className="list-disc pl-6 text-gray-700 space-y-2 mb-4">
                <li>Ter pelo menos 18 anos de idade ou estar devidamente representado</li>
                <li>Fornecer informações verdadeiras, precisas e atualizadas</li>
                <li>Manter a segurança de sua conta e senha</li>
                <li>Notificar imediatamente sobre uso não autorizado de sua conta</li>
                <li>Ser responsável por todas as atividades realizadas em sua conta</li>
              </ul>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4 flex items-center">
                <AlertTriangle className="w-6 h-6 mr-2 text-blue-600" />
                4. Uso Aceitável
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Você concorda em NÃO utilizar o Sistema para:
              </p>
              <ul className="list-disc pl-6 text-gray-700 space-y-2 mb-4">
                <li>Atividades ilegais ou não autorizadas</li>
                <li>Violar direitos de propriedade intelectual de terceiros</li>
                <li>Transmitir vírus, malware ou código malicioso</li>
                <li>Tentar acessar áreas restritas do sistema sem autorização</li>
                <li>Realizar engenharia reversa ou tentar extrair código-fonte</li>
                <li>Interferir ou interromper o funcionamento do Sistema</li>
                <li>Coletar dados de outros usuários sem autorização</li>
                <li>Usar o Sistema para spam ou comunicações não solicitadas</li>
              </ul>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4">
                5. Propriedade Intelectual
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Todo o conteúdo do Sistema, incluindo mas não limitado a textos, gráficos, logos, 
                ícones, imagens, código-fonte e software, é propriedade da CRM Shot Fair Brasil ou 
                de seus licenciadores e está protegido por leis de direitos autorais e propriedade 
                intelectual.
              </p>
              <p className="text-gray-700 leading-relaxed mb-4">
                Você não pode copiar, modificar, distribuir, vender ou alugar qualquer parte do 
                Sistema sem autorização prévia por escrito.
              </p>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4">
                6. Dados e Conteúdo do Usuário
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Você mantém todos os direitos sobre os dados e conteúdo que você insere no Sistema. 
                Ao utilizar o Sistema, você concede à CRM Shot Fair Brasil uma licença não exclusiva 
                para usar, armazenar e processar seus dados exclusivamente para fornecer e melhorar 
                os serviços.
              </p>
              <p className="text-gray-700 leading-relaxed mb-4">
                Você é responsável por garantir que possui todos os direitos necessários sobre os 
                dados que insere no Sistema e que não viola direitos de terceiros.
              </p>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4">
                7. Privacidade e Proteção de Dados
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                O tratamento de seus dados pessoais é realizado em conformidade com a Lei Geral de 
                Proteção de Dados (LGPD). Para mais informações sobre como coletamos, usamos e 
                protegemos seus dados, consulte nossa{' '}
                <Link to="/politica-privacidade" className="text-blue-600 hover:underline">
                  Política de Privacidade
                </Link>.
              </p>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4">
                8. Disponibilidade do Serviço
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Embora nos esforcemos para manter o Sistema disponível 24/7, não garantimos 
                disponibilidade ininterrupta. O Sistema pode estar temporariamente indisponível 
                devido a:
              </p>
              <ul className="list-disc pl-6 text-gray-700 space-y-2 mb-4">
                <li>Manutenção programada ou de emergência</li>
                <li>Falhas técnicas ou de infraestrutura</li>
                <li>Eventos fora de nosso controle (força maior)</li>
                <li>Atualizações e melhorias do sistema</li>
              </ul>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4">
                9. Limitação de Responsabilidade
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                A CRM Shot Fair Brasil não se responsabiliza por:
              </p>
              <ul className="list-disc pl-6 text-gray-700 space-y-2 mb-4">
                <li>Perdas ou danos decorrentes do uso ou impossibilidade de uso do Sistema</li>
                <li>Interrupções, erros ou falhas no Sistema</li>
                <li>Perda de dados devido a falhas técnicas (recomendamos backups regulares)</li>
                <li>Decisões tomadas com base em informações geradas pelo Sistema</li>
                <li>Ações de terceiros que acessem sua conta sem autorização</li>
              </ul>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4 flex items-center">
                <XCircle className="w-6 h-6 mr-2 text-blue-600" />
                10. Encerramento de Conta
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Você pode solicitar o encerramento de sua conta a qualquer momento. A CRM Shot Fair 
                Brasil também pode suspender ou encerrar sua conta em caso de:
              </p>
              <ul className="list-disc pl-6 text-gray-700 space-y-2 mb-4">
                <li>Violação destes Termos de Uso</li>
                <li>Uso fraudulento ou ilegal do Sistema</li>
                <li>Inatividade prolongada da conta</li>
                <li>Solicitação de autoridades competentes</li>
              </ul>
              <p className="text-gray-700 leading-relaxed mb-4">
                Após o encerramento, seus dados serão mantidos conforme exigido por lei e nossa 
                Política de Privacidade.
              </p>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4">
                11. Modificações nos Termos
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Reservamos o direito de modificar estes Termos de Uso a qualquer momento. Alterações 
                significativas serão comunicadas através do Sistema ou por e-mail. O uso continuado 
                do Sistema após as alterações constitui aceitação dos novos termos.
              </p>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4">
                12. Lei Aplicável e Foro
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Estes Termos de Uso são regidos pelas leis brasileiras. Qualquer disputa será 
                resolvida no foro da comarca de Joinville, Estado de Santa Catarina, Brasil.
              </p>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4">
                13. Contato
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Para questões relacionadas a estes Termos de Uso, entre em contato:
              </p>
              <div className="bg-gray-50 p-4 rounded-lg">
                <p className="text-gray-700 mb-2"><strong>E-mail:</strong> suporte@crmshot.com</p>
                <p className="text-gray-700 mb-2"><strong>Telefone:</strong> (47) 99999-9999</p>
                <p className="text-gray-700"><strong>Endereço:</strong> Joinville, SC, Brasil</p>
              </div>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4">
                14. Disposições Gerais
              </h2>
              <ul className="list-disc pl-6 text-gray-700 space-y-2 mb-4">
                <li>Se qualquer disposição destes termos for considerada inválida, as demais permanecerão em vigor</li>
                <li>Estes termos constituem o acordo completo entre você e a CRM Shot Fair Brasil</li>
                <li>A tolerância com o descumprimento de qualquer termo não constitui renúncia</li>
                <li>Você não pode transferir seus direitos ou obrigações sem autorização prévia</li>
              </ul>
            </section>

            <div className="mt-8 pt-6 border-t border-gray-200">
              <p className="text-sm text-gray-500 mb-4">
                Ao utilizar o Sistema, você declara que leu, compreendeu e concorda com estes Termos de Uso.
              </p>
              <div className="flex items-center text-sm text-gray-600">
                <Shield className="w-4 h-4 mr-2" />
                <span>
                  Estes termos estão em conformidade com a LGPD e demais legislações brasileiras aplicáveis.
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default TermosUso;

