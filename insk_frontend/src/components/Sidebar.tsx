// src/components/Sidebar.tsx

import Link from 'next/link'; // Next.js의 페이지 이동을 위한 Link 컴포넌트

export default function Sidebar() {
  const menuItems = [
    { name: '종합 대시보드', path: '/' },
    { name: '트렌드 상세 분석', path: '/trends' },
    { name: '인사이트 상세 분석', path: '/insights' },
  ];

  return (
    <aside className="w-64 bg-gray-900 text-white p-4">
      <h2 className="text-2xl font-bold mb-6">INSK v2.0</h2>
      <nav>
        <ul>
          {menuItems.map(item => (
            <li key={item.name} className="mb-2">
              <Link href={item.path} className="block p-2 rounded-lg hover:bg-gray-700">
                {item.name}
              </Link>
            </li>
          ))}
        </ul>
      </nav>
    </aside>
  );
}