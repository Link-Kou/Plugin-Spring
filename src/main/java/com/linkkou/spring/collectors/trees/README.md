### 示列

```java:

final List<TypeConfigListTreeVO> build = new Trees().from(otherTitleList)
                .to(new ArrayList<TypeConfigListTreeVO>())
                .getId(x -> Optional.ofNullable(x).map(o -> o.getId()).orElse(""))
                .getParentId(x -> Optional.ofNullable(x).map(o -> o.getParentId()).orElse(""))
                .setTranslate(x -> new TypeConfigListTreeVO().setId(x.getId()).setTitle(x.getTitle()).setPreId(x.getPreId()))
                .getSort((x, y) -> {
                    if ("".equals(x.getPreId())) {
                        return -1;
                    }
                    if (Objects.equals(x.getId(), y.getPreId())) {
                        return -1;
                    }
                    if (Objects.equals(x.getPreId(), y.getId())) {
                        return 1;
                    }
                    return x.getId().compareTo(y.getPreId());
                })
                .setChildren((x, y) -> {
                    if (Optional.ofNullable(x.getChildren()).isPresent()) {
                        x.getChildren().addAll(y);
                    } else {
                        x.setChildren(y);
                    }
                    return x.getChildren();
                }).build("", x -> x.getId());


```

### 使用方法待续
