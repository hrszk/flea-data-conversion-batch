
# [旧商品データ]テーブルからの[カテゴリーデータ]テーブル作成の要件


### 1. DB の準備

[ER 図](#er-図) の内容を参考に、DDL を作成してください。

DDL には、以下のテーブルを含めてください。DB 名は自由に決めてください。

- original
- category
- items
- users

### 2. TSV ファイルのデータを original テーブルに取り込む

TSV ファイルの全レコードを、original テーブルに挿入してください。

### 3. original テーブルから category テーブルにレコードを取り込む

original テーブルから category テーブルにカテゴリ情報を取り込んでください。

original テーブルには `/` 区切りで商品のカテゴリが記載されたカラムがあります。

```plaintext
Men/Tops/T-shirts
```

これらを次の形式で category テーブルに格納してください。

| id  | name    | parent_id | name_all          |
| --- | ------- | --------- | ----------------- |
| 1   | Men     | null      | null              |
| 2   | Tops    | 1         | null              |
| 3   | T-shirt | 2         | Men/Tops/T-shirts |

- 最上位カテゴリの `Men` には、parent_id、name_all に `null` を設定
- 次のカテゴリである `Tops` には、親である `Men` の id が parent_id として入り、name_all は `null` を設定
- 最下位カテゴリである `T-shirts` の parent_id は `Tops` の id が入り、name_all には**上層カテゴリの name をスラッシュで連結した文字列**を設定

ただし、original テーブル上のデータでカテゴリが 4 つ以上に分解できるデータについては、3 つ目のカテゴリまでを category テーブルに格納してください。

```plaintext
例:
Men/Clothes/Tops/T-shirts
-> Men/Clothes/Tops までカテゴリテーブルに追加する。T-shirts は追加しない。
```

### 4. original テーブルから items テーブルにレコードを取り込む

original テーブルから items テーブルに商品情報を取り込んでください。
`original.conditino_id` は、値をそのまま `items.condition` に設定してください。

### ER 図

```mermaid
---
title: 商品データ管理 DB
---
erDiagram


    items {
        int id
        string name
        int condition
        int category
        string brand
        double price
        int stock
        int shipping
        string description
    }

    category {
        int id
        int parent_id
        string name
        string name_all
    }

    original {
        int id
        string name
        int condition_id
        string category_name
        string brand
        double price
        int shipping
        string description
    }

    category ||--o{ category : has
    category ||--o{ items : has
```
