#!/bin/bash

# 项目重命名脚本
# 功能：将template-spring-cloud项目重命名为指定项目名，并更新相应的包名
# 用法：./rename-project.sh <项目名> (项目名必须使用中划线分隔)

# 检查是否提供了项目名参数
if [ -z "$1" ]; then
  echo "错误: 请提供项目名参数，使用中划线分隔"
  echo "用法: $0 <项目名>"
  exit 1
fi

# 检查项目名格式是否正确（只能包含字母、数字和中划线）
if ! [[ "$1" =~ ^[a-zA-Z0-9]+(-[a-zA-Z0-9]+)*$ ]]; then
  echo "错误: 项目名格式不正确，只能包含字母、数字和中划线，且不能以中划线开头或结尾"
  exit 1
fi

PROJECT_NAME="$1"
# 将项目名转换为点号形式（用于包名）
PACKAGE_NAME=$(echo "$PROJECT_NAME" | tr '-' '.')

# 替换前的项目名和包名
OLD_PROJECT_NAME="template-spring-cloud"
OLD_PACKAGE_NAME="template_spring_cloud"
# 根包名前缀
ROOT_PACKAGE_PREFIX="io.bloom"

# 当前脚本文件名
SCRIPT_NAME=$(basename "$0")

# 定义对号符号
CHECKMARK="✓"
NOCHECKMARK="✗"

# 显示详细文件列表的开关 (true/false)
SHOW_DETAILS=true

echo "开始重命名项目: $OLD_PROJECT_NAME -> $PROJECT_NAME"

# 查找并替换项目名
PROJECT_FILES=$(find . -type f ! -name "$SCRIPT_NAME" ! -path "./.*" ! -path "./*/.*" -exec grep -l "$OLD_PROJECT_NAME" {} +)
if [ -n "$PROJECT_FILES" ]; then
  find . -type f ! -name "$SCRIPT_NAME" ! -path "./.*" ! -path "./*/.*" -print0 | xargs -0 env LC_ALL=C sed -i '' "s/$OLD_PROJECT_NAME/$PROJECT_NAME/g"
  echo "${CHECKMARK} 已更新项目名: $OLD_PROJECT_NAME -> $PROJECT_NAME"
  if [ "$SHOW_DETAILS" = true ]; then
    echo "更新的文件:"
    echo "$PROJECT_FILES"
  fi
else
  echo "${NOCHECKMARK} 没有找到需要更新项目名的文件"
fi

echo -e "\n============================\n"

# 查找并替换包名
PACKAGE_FILES=$(find ./src -type f -exec grep -l "$ROOT_PACKAGE_PREFIX\.$OLD_PACKAGE_NAME" {} +)
if [ -n "$PACKAGE_FILES" ]; then
  find ./src -type f -print0 | xargs -0 env LC_ALL=C sed -i '' "s/$ROOT_PACKAGE_PREFIX\.$OLD_PACKAGE_NAME/$ROOT_PACKAGE_PREFIX\.$PACKAGE_NAME/g"
  echo "${CHECKMARK} 已更新包名: $ROOT_PACKAGE_PREFIX.$OLD_PACKAGE_NAME -> $ROOT_PACKAGE_PREFIX.$PACKAGE_NAME"
  if [ "$SHOW_DETAILS" = true ]; then
    echo "更新的文件:"
    echo "$PACKAGE_FILES"
  fi
else
  echo "${NOCHECKMARK} 没有找到需要更新包名的文件"
fi


echo -e "\n============================\n"

# 更新主代码包目录结构
OLD_PACKAGE_DIR="src/main/java/${ROOT_PACKAGE_PREFIX//.//}/$OLD_PACKAGE_NAME"
if [ -d "$OLD_PACKAGE_DIR" ]; then
  # 将点号分隔的包名转换为目录路径
  PACKAGE_DIR=$(echo "$PACKAGE_NAME" | tr '.' '/')
  NEW_PACKAGE_DIR="src/main/java/${ROOT_PACKAGE_PREFIX//.//}/$PACKAGE_DIR"
  mkdir -p "$NEW_PACKAGE_DIR"
  mv "$OLD_PACKAGE_DIR"/* "$NEW_PACKAGE_DIR/"
  rm -rf "$OLD_PACKAGE_DIR"
  echo "${CHECKMARK} 已更新主代码包目录结构"
  if [ "$SHOW_DETAILS" = true ]; then
    echo "从 $OLD_PACKAGE_DIR 移动到 $NEW_PACKAGE_DIR"
  fi
else
  echo "${NOCHECKMARK} 未找到主代码包目录"
fi


echo -e "\n============================\n"

# 更新测试代码包目录结构
OLD_TEST_PACKAGE_DIR="src/test/java/${ROOT_PACKAGE_PREFIX//.//}/$OLD_PACKAGE_NAME"
if [ -d "$OLD_TEST_PACKAGE_DIR" ]; then
  # 将点号分隔的包名转换为目录路径
  PACKAGE_DIR=$(echo "$PACKAGE_NAME" | tr '.' '/')
  NEW_TEST_PACKAGE_DIR="src/test/java/${ROOT_PACKAGE_PREFIX//.//}/$PACKAGE_DIR"
  mkdir -p "$NEW_TEST_PACKAGE_DIR"
  mv "$OLD_TEST_PACKAGE_DIR"/* "$NEW_TEST_PACKAGE_DIR/"
  rm -rf "$OLD_TEST_PACKAGE_DIR"
  echo "${CHECKMARK} 已更新测试代码包目录结构"
  if [ "$SHOW_DETAILS" = true ]; then
    echo "从 $OLD_TEST_PACKAGE_DIR 移动到 $NEW_TEST_PACKAGE_DIR"
  fi
else
  echo "${NOCHECKMARK} 未找到测试代码包目录"
fi


echo -e "\n============================\n"

echo "项目重命名完成！"