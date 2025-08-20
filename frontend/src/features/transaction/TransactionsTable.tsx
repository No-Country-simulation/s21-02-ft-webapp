// src/components/transactions/TransactionsTable.tsx
interface Transaction {
    id: string;
    commerce: string;
    date: string;
    amount: string;
  }
  
  export const TransactionsTable = () => {
    const transactions: Transaction[] = [
      { id: '1', commerce: 'Comercio', date: '24/07/2023', amount: '150$' },
      { id: '2', commerce: 'Comercio', date: '24/06/2023', amount: '15$' },
      { id: '3', commerce: 'Comercio', date: '02/05/2023', amount: '50$' },
    ];
  
    return (
      <div className="bg-white rounded-lg p-4 shadow-md my-4">
        <table className="table-auto w-full">
          <thead>
            <tr>
              <th className="px-4 py-2 text-left border-b-2 w-full">
                <h2 className="text-ml font-bold text-gray-600">Transacciones</h2>
              </th>
            </tr>
          </thead>
          <tbody>
            {transactions.map((transaction) => (
              <tr key={transaction.id} className="border-b w-full">
                <td className="px-4 py-2 text-left align-top w-1/2">
                  <div>
                    <h2>{transaction.commerce}</h2>
                    <p className="text-gray-500">{transaction.date}</p>
                  </div>
                </td>
                <td className="px-4 py-2 text-right text-cyan-500 w-1/2">
                  <p><span>{transaction.amount}</span></p>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
  };