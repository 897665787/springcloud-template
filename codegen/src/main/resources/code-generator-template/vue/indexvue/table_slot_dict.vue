        <template #accountType="{ row }">
          {{ {columnName}Map.get(row.{columnName}.toString()) }}
        </template>
