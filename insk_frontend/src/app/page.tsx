// app/page.tsx

"use client"; // useState는 클라이언트 컴포넌트에서만 사용 가능합니다. 파일 최상단에 추가!

import { useState } from 'react'; // useState를 import 합니다.
import articles from '../../mock-articles.json';
import ArticleCard from '../components/ArticleCard';

export default function HomePage() {
  // 1. 상태(State) 정의: 사용자가 선택한 카테고리를 기억할 공간. 기본값은 'All'
  const [selectedCategory, setSelectedCategory] = useState('All');

  const categories = ['All', 'Telco', 'LLM', 'INFRA'];

  // 2. 필터링 로직: 선택된 카테고리가 'All'이 아니면, 해당 카테고리의 기사만 거름.
  const filteredArticles = selectedCategory === 'All'
    ? articles
    : articles.filter(article => article.category === selectedCategory);

  return (
    // Tailwind CSS를 이용해 기본적인 레이아웃과 디자인을 추가합니다.
    <main className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-4">INSK v2.0 대시보드</h1>

      {/* 3. 필터 UI: 카테고리 버튼들 */}
      <div className="flex space-x-2 mb-4">
        {categories.map(category => (
          <button
            key={category}
            onClick={() => setSelectedCategory(category)} // 버튼을 누르면 selectedCategory 상태를 변경!
            className={`px-4 py-2 rounded-lg ${selectedCategory === category ? 'bg-blue-600 text-white' : 'bg-gray-700 text-gray-300'}`}
          >
            {category}
          </button>
        ))}
      </div>

      {/* 4. 필터링된 기사 목록 렌더링 */}
      <div>
        {filteredArticles.map(article => (
          <ArticleCard
            key={article.id}
            title={article.title}
            summary={article.summary}
          />
        ))}
      </div>
    </main>
  );
}